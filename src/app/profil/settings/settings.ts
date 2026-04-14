import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { AlertService } from '../../services/alert.service';
import { TicketService } from '../../services/ticket.service';
import { GalleryService } from '../../services/gallery.service';

@Component({
  selector: 'app-settings',
  imports: [FormsModule],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})
export class Settings implements OnInit {
  private auth = inject(AuthService);
  private userService = inject(UserService);
  private alertService = inject(AlertService);
  private ticketService = inject(TicketService);
  private galleryService = inject(GalleryService);
  currentUser = toSignal(this.auth.currentUser$);

  // Profile form
  firstName: string = '';
  lastName: string = '';
  phoneNumber: string = '';
  profileLoading: boolean = false;

  // Password form
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  passwordLoading: boolean = false;
  showCurrentPassword: boolean = false;
  showNewPassword: boolean = false;
  showConfirmPassword: boolean = false;

  // Description
  description: string = '';

  // Wage
  wage: number | null = null;

  // Admin ticket form
  ticketName: string = '';
  ticketLength: number | null = null;
  ticketPrice: number | null = null;
  ticketLoading: boolean = false;

  // Admin gallery form
  galleryAltText: string = '';
  galleryFile: File | null = null;
  galleryLoading: boolean = false;

  passwordRegex = /^(?=.*[0-9])(?=.*[^a-zA-Z0-9\s]).{8,}$/;
  phoneRegex = /^\+?[0-9\s\-]{7,15}$/;

  private authService = inject(AuthService);

  currentUserRole = toSignal(this.authService.currentUser$);

  get phoneNumberError(): string {
    if (!this.phoneNumber) return '';
    if (!this.phoneRegex.test(this.phoneNumber)) {
      return 'Érvénytelen telefonszám formátum (pl. +36 20 123 4567)';
    }
    return '';
  }

  get isProfileFormValid(): boolean {
    return !!this.firstName.trim() && !!this.lastName.trim();
  }

  get currentPasswordError(): string {
    if (!this.currentPassword) return '';
    return '';
  }

  get newPasswordError(): string {
    if (!this.newPassword) return '';
    if (!this.passwordRegex.test(this.newPassword)) {
      return 'A jelszónak legalább 8 karakter hosszúnak kell lennie, és tartalmaznia kell egy számot és egy speciális karaktert';
    }
    return '';
  }

  get confirmPasswordError(): string {
    if (!this.confirmPassword) return '';
    if (this.newPassword !== this.confirmPassword) {
      return 'A két jelszó nem egyezik';
    }
    return '';
  }

  get isPasswordFormValid(): boolean {
    return (
      !!this.currentPassword &&
      !!this.newPassword &&
      !!this.confirmPassword &&
      !this.newPasswordError &&
      !this.confirmPasswordError
    );
  }

  get isTicketFormValid(): boolean {
    return (
      !!this.ticketName.trim() &&
      this.ticketLength !== null &&
      this.ticketLength > 0 &&
      this.ticketPrice !== null &&
      this.ticketPrice > 0
    );
  }

  get isGalleryFormValid(): boolean {
    return !!this.galleryAltText.trim() && !!this.galleryFile;
  }

  ngOnInit() {
    const user = this.currentUser();
    if (user) {
      this.firstName = user.firstName || '';
      this.lastName = user.lastName || '';
      this.phoneNumber = user.phoneNumber || '';
    }
  }

  changeUserName() {
    if (!this.isProfileFormValid) return;

    this.profileLoading = true;
    this.userService
      .updateUserName({
        firstName: this.firstName.trim(),
        lastName: this.lastName.trim(),
      })
      .subscribe({
        next: () => {
          this.alertService.success('A felhasználó név sikeresen frissítve!');
          this.profileLoading = false;
          this.auth.refreshCurrentUser();
        },
        error: (err) => {
          console.error('Felhasználó név frissítése sikertelen:', err);
          this.alertService.error('Felhasználó név frissítése sikertelen!');
          this.profileLoading = false;
        },
      });
  }

  changePhoneNumber() {
    if (this.phoneNumberError) return;

    const payLoad = { number: this.phoneNumber.trim() };

    this.userService.updatePhoneNumber(payLoad).subscribe({
      next: () => {
        this.alertService.success('Telefonszám sikeresen frissítve!');
        this.auth.refreshCurrentUser();
      },
      error: (err) => {
        console.error('Telefonszám frissítése sikertelen:', err);
        this.alertService.error('Telefonszám frissítése sikertelen!');
      },
    });
  }

  changePassword() {
    if (!this.isPasswordFormValid) return;

    this.passwordLoading = true;
    this.userService
      .changePassword({
        oldPassword: this.currentPassword,
        password1: this.newPassword,
        password2: this.confirmPassword,
      })
      .subscribe({
        next: () => {
          this.alertService.success('Jelszó sikeresen megváltoztatva!');
          this.currentPassword = '';
          this.newPassword = '';
          this.confirmPassword = '';
          this.passwordLoading = false;
        },
        error: (err) => {
          console.error('Jelszó változtatás sikertelen:', err);
          this.alertService.error(
            'Jelszó változtatás sikertelen! Ellenőrizd a jelenlegi jelszavad.',
          );
          this.passwordLoading = false;
        },
      });
  }

  togglePassword(field: 'current' | 'new' | 'confirm') {
    if (field === 'current') this.showCurrentPassword = !this.showCurrentPassword;
    if (field === 'new') this.showNewPassword = !this.showNewPassword;
    if (field === 'confirm') this.showConfirmPassword = !this.showConfirmPassword;
  }

  uploadProfilePicture(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    const file = input.files[0];
    this.userService.changeProfilPicture(file).subscribe({
      next: () => {
        this.alertService.success('Profilkép sikeresen frissítve!');
        this.auth.refreshCurrentUser();
      },
      error: (err) => {
        console.error('Profilkép frissítése sikertelen:', err);
        this.alertService.error('Profilkép frissítése sikertelen!');
      },
    });
  }

  changeDescription() {
    if (this.description.length < 0) {
      this.alertService.error('Kérem, adjon meg leírást!');
      return;
    }

    this.userService.changeDescription(this.description).subscribe({
      next: () => {
        this.alertService.success('Leírás sikeresen frissítve!');
        this.auth.refreshCurrentUser();
      },
      error: (err) => {
        console.error('Leírás frissítése sikertelen:', err);
        this.alertService.error('Leírás frissítése sikertelen!');
      },
    });
  }

  changeWage() {
    if (this.wage === null || this.wage < 1000) {
      this.alertService.error('Kérem, adjon meg egy érvényes órabért!');
      return;
    }
    this.userService.changeWage(this.wage).subscribe({
      next: () => {
        this.alertService.success('Órabér sikeresen frissítve!');
        this.auth.refreshCurrentUser();
      },
      error: (err) => {
        console.error('Órabér frissítése sikertelen:', err);
        this.alertService.error('Órabér frissítése sikertelen!');
      },
    });
  }

  addTicket() {
    if (!this.isTicketFormValid) return;

    this.ticketLoading = true;
    this.ticketService
      .addTicket({
        price: this.ticketPrice!,
        ticketName: this.ticketName.trim(),
        unit: 'nap',
        validityLength: this.ticketLength!
        
      })
      .subscribe({
        next: () => {
          this.alertService.success('Ticket sikeresen hozzáadva!');
          this.ticketName = '';
          this.ticketLength = null;
          this.ticketPrice = null;
          this.ticketLoading = false;
        },
        error: (err) => {
          console.error('Ticket hozzáadása sikertelen:', err);
          this.alertService.error('Ticket hozzáadása sikertelen!');
          this.ticketLoading = false;
        },
      });
  }

  onGalleryFileChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;

    this.galleryFile = input.files[0];
  }

  uploadGaleryPicture() {
    if (!this.isGalleryFormValid || !this.galleryFile) return;

    this.galleryLoading = true;
    this.galleryService.addGalleryImg(this.galleryFile, this.galleryAltText.trim()).subscribe({
      next: () => {
        this.alertService.success('Galéria kép sikeresen feltöltve!');
        this.galleryAltText = '';
        this.galleryFile = null;
        this.galleryLoading = false;
      },
      error: (err) => {
        console.error('Galéria kép feltöltése sikertelen:', err);
        this.alertService.error('Galéria kép feltöltése sikertelen!');
        this.galleryLoading = false;
      },
    });
  }





}
