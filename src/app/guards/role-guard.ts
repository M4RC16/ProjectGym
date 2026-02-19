import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard: CanActivateFn = (route, state) => {
  
  const auth = inject(AuthService);
  const router = inject(Router);

  const expectedRole = route.data['role'];
  const userRole = auth.getCurrentUser()?.role?.[0]?.id;

  if (!auth.isAuthenticated()) {
    router.navigate(['/login']);
    return false;
  }

  if (userRole !== expectedRole) {
    router.navigate(['/login']);
    return false;
  }

  return true;

};
