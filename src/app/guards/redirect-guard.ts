import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { inject } from '@angular/core';

export const redirectGuard: CanActivateFn = (): UrlTree => {
  const router = inject(Router);
  const role = localStorage.getItem('loggedInUser');

  switch (role) {
    case '1':
      return router.createUrlTree(['/profil/admin']);
    case '2':
      return router.createUrlTree(['/profil/trainer']);
    default:
      return router.createUrlTree(['/profil/user']);
  }
};
