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
    path: 'profil',
    component: Profil,
    children: [
      {
        path: 'admin',
        component: Calendar,
      },
    ],
  },
  {
    path: '**',
    component: NotFound,
  },
];
