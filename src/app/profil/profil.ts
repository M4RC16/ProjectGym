import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterOutlet, RouterLinkWithHref, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { AuthService } from '../services/auth.service';
import { filter, map, startWith } from 'rxjs';
import { UserService } from '../services/user.service';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'app-profil',
  imports: [RouterOutlet, RouterLinkWithHref, RouterLinkActive, FormsModule],
  templateUrl: './profil.html',
  styleUrl: './profil.css',
})
export class Profil {

  private auth = inject(AuthService);
  private router = inject(Router);
  private userService = inject(UserService);
  private alertService = inject(AlertService);

  currentUser = toSignal(this.auth.currentUser$);



  firstName = '';
  lastName = '';
  phoneNumber = '';
  selectedProfileImage: File | null = null;
  profileFormLoading = false;

  phoneRegex = /^\+?[0-9\s\-]{7,15}$/;

  isCalendarRoute = toSignal(
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd),
      startWith(null),
      map(() => {
        const url = this.router.url;
        return url.includes('/profil/user') || url.includes('/profil/trainer');
      })
    ),
    { initialValue: false }
  );

  shouldShowProfileCompletionForm(): boolean {
    const user = this.currentUser();
    if (!user) return false;

    return !user.firstName || !user.lastName || !user.phoneNumber;
  }

  get isProfileCompletionFormValid(): boolean {
    return (
      !!this.firstName.trim() &&
      !!this.lastName.trim() &&
      !!this.phoneNumber.trim() &&
      this.phoneRegex.test(this.phoneNumber.trim())
    );
  }

  get phoneNumberError(): string {
    if (!this.phoneNumber.trim()) return '';
    if (!this.phoneRegex.test(this.phoneNumber.trim())) {
      return 'Érvénytelen telefonszám formátum (pl. +36 20 123 4567)';
    }
    return '';
  }

  onProfileImageChange(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) {
      this.selectedProfileImage = null;
      return;
    }

    this.selectedProfileImage = input.files[0];
  }

  saveProfileCompletion() {
    if (!this.isProfileCompletionFormValid) return;

    this.profileFormLoading = true;
    this.userService
      .updateUserName({
        firstName: this.firstName.trim(),
        lastName: this.lastName.trim(),
      })
      .subscribe({
        next: () => {
          this.userService.updatePhoneNumber({ number: this.phoneNumber.trim() }).subscribe({
            next: () => {
              if (this.selectedProfileImage) {
                this.userService.changeProfilPicture(this.selectedProfileImage).subscribe({
                  next: () => {
                    this.alertService.success('Profil adatok sikeresen mentve!');
                    this.profileFormLoading = false;
                    this.auth.refreshCurrentUser();
                  },
                  error: (err) => {
                    console.error('Profilkép mentése sikertelen:', err);
                    this.alertService.error('A profilkép mentése sikertelen!');
                    this.profileFormLoading = false;
                  },
                });
                return;
              }

              this.alertService.success('Profil adatok sikeresen mentve!');
              this.profileFormLoading = false;
              this.auth.refreshCurrentUser();
            },
            error: (err) => {
              console.error('Telefonszám mentése sikertelen:', err);
              this.alertService.error('A telefonszám mentése sikertelen!');
              this.profileFormLoading = false;
            },
          });
        },
        error: (err) => {
          console.error('Név mentése sikertelen:', err);
          this.alertService.error('A név mentése sikertelen!');
          this.profileFormLoading = false;
        },
      });
  }

  constructor() {
      console.log('Current user in Profil component:', this.currentUser()?.profilePicture);
    const user = this.currentUser();
    if (user) {
      this.firstName = user.firstName || '';
      this.lastName = user.lastName || '';
      this.phoneNumber = user.phoneNumber || '';
    }
  }

  logout() {
    this.auth.logout(); 
  }


}
