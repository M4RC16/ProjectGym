import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLinkWithHref } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-profil',
  imports: [RouterOutlet, RouterLinkWithHref],
  templateUrl: './profil.html',
  styleUrl: './profil.css',
})
export class Profil {

  private auth = inject(AuthService);
  currentUser = toSignal(this.auth.currentUser$);

  logout() {
    this.auth.logout(); 
  }


}
