import { Component, signal} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-login',
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  password = signal('');
  email = signal('');

  onLogin() {
    console.log('Logging in with', this.email(), this.password());
  }

}
