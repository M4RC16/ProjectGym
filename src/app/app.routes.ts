import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { NotFound } from './not-found/not-found';
import { Landing } from './landing/landing';
import { Gallery } from './gallery/gallery';

export const routes: Routes = [
    {
        path: '',
        component: Landing
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
        path: '**',
        component: NotFound,
    }
];
