import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-register',
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  email = signal('');
  password = signal('');
  confirmPassword = signal('');
  emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
  passwordRegex = "^(?=.[0-9])(?=.[!@#$%^&*]).{8,}$";

  onRegister() {

    if (this.emailRegex.match(this.email())) {

    }
    console.log('Registering with', this.email(), this.password(), this.confirmPassword());
  }

}
