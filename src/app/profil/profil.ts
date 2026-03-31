import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLinkWithHref, RouterLinkActive, Router, NavigationEnd } from '@angular/router';
import { toSignal } from '@angular/core/rxjs-interop';
import { AuthService } from '../services/auth.service';
import { filter, map, startWith } from 'rxjs';

@Component({
  selector: 'app-profil',
  imports: [RouterOutlet, RouterLinkWithHref, RouterLinkActive],
  templateUrl: './profil.html',
  styleUrl: './profil.css',
})
export class Profil {

  private auth = inject(AuthService);
  private router = inject(Router);

  currentUser = toSignal(this.auth.currentUser$);

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

  logout() {
    this.auth.logout(); 
  }


}
