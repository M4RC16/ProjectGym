import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { NotFound } from './not-found/not-found';
import { Landing } from './landing/landing';
import { Gallery } from './gallery/gallery';
import { Tickets } from './tickets/tickets';
import { ContactUs } from './contact-us/contact-us';
import { Trainers } from './trainers/trainers';
import { Profil } from './profil/profil';
import { Calendar } from './profil/calendar/calendar';
import { Dashboard } from './profil/dashboard/dashboard';
import { Settings } from './profil/settings/settings';
import { Messages } from './profil/messages/messages';
import { authGuard } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';
import { redirectGuard } from './guards/redirect-guard';
import { ForgottenPass } from './login/forgotten-pass/forgotten-pass';

export const routes: Routes = [
  {
    path: '',
    component: Landing,
  },
  {
    path: 'login',
    component: Login,
  },
  {
    path: 'register',
    component: Register,
  },
  {
    path: 'gallery',
    component: Gallery,
  },
  {
    path: 'tickets',
    component: Tickets,
  },
  {
    path: 'contact-us',
    component: ContactUs,
  },
  {
    path: 'trainers',
    component: Trainers,
  },
    {
    path: 'forgotten-password',
    component: ForgottenPass,
  },
    {
      path: 'forgotten-password/reset',
      component: ForgottenPass,
    },
  {
    path: 'profil',
    component: Profil,
    canActivate: [authGuard],
    children: [
      {
        path: '',
        canActivate: [redirectGuard],
        children: [],
      },
      {
        path: 'user',
        component: Calendar,
        data: { role: 3 },
        canActivate: [roleGuard],
      },
      {
        path: 'trainer',
        component: Calendar,
        data: { role: 2 },
        canActivate: [roleGuard],
      },
      {
        path: 'admin',
        component: Dashboard,
        data: { role: 1 },
        canActivate: [roleGuard],
      },
      {
        path: 'settings',
        component: Settings,
      },
      {
        path: 'messages',
        component: Messages,
        data: { role: 1 },
        canActivate: [roleGuard],
      },
    ],
  },
  {
    path: '**',
    component: NotFound,
  },
];
