import { Component, inject, signal } from '@angular/core';
import { UserFormData } from '../models/models.model';
import { FormsModule } from "@angular/forms";
import { UserService } from '../services/user.service';
import { AlertService } from '../services/alert.service';

@Component({
  selector: 'app-contact-us',
  imports: [FormsModule],
  templateUrl: './contact-us.html',
  styleUrl: './contact-us.css',
})
export class ContactUs {

  private userService = inject(UserService);
  private alertService = inject(AlertService);

  formdata: UserFormData = {
    firstName: '',
    lastName: '',
    emailAddress: '',
    phoneNumber: '',
    message: ''
  }

  lastNameError = signal('');
  firstNameError = signal('');
  emailError = signal('');
  phoneError = signal('');
  messageError = signal('');

  private emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  private phoneRegex = /^(\+36|06)[ -]?(1|20|30|31|50|70)[ -]?\d{3}[ -]?\d{4}$/;

  sendForm() {
    this.lastNameError.set('');
    this.firstNameError.set('');
    this.emailError.set('');
    this.phoneError.set('');
    this.messageError.set('');

    let hasError = false;

    if (!this.formdata.lastName.trim()) {
      this.lastNameError.set('Add meg a vezetékneved');
      hasError = true;
    }

    if (!this.formdata.firstName.trim()) {
      this.firstNameError.set('Add meg a keresztneved');
      hasError = true;
    }

    if (!this.formdata.emailAddress.trim()) {
      this.emailError.set('Add meg az email címed');
      hasError = true;
    } else if (!this.emailRegex.test(this.formdata.emailAddress)) {
      this.emailError.set('Érvénytelen email formátum');
      hasError = true;
    }

    if (!this.formdata.phoneNumber.trim()) {
      this.phoneError.set('Add meg a telefonszámod');
      hasError = true;
    } else if (!this.phoneRegex.test(this.formdata.phoneNumber)) {
      this.phoneError.set('Érvénytelen telefonszám formátum (pl. +36 30 123 4567)');
      hasError = true;
    }

    if (!this.formdata.message.trim()) {
      this.messageError.set('Írd be az üzeneted');
      hasError = true;
    }

    if (hasError) return;

    this.userService.sendUserForm(this.formdata).subscribe({
      next: () => {
        this.alertService.success('Üzeneted sikeresen elküldve!');
        this.formdata = {
          firstName: '',
          lastName: '',
          emailAddress: '',
          phoneNumber: '',
          message: ''
        };
      },
      error: (error) => {
        console.error('Hiba az üzenet küldésekor:', error);
        this.alertService.error('Hiba történt az üzenet küldésekor. Kérlek, próbáld újra később.');
      },
    });
  }

}
