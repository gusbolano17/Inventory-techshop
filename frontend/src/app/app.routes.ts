import { Routes } from '@angular/router';
import { authGuard } from './core/auth-guard';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: 'login',
    loadComponent: () => import('./modules/login/login').then((m) => m.Login),
  },
  {
    path: 'main',
    canActivate: [authGuard],
    loadComponent: () => import('./modules/main/main').then((m) => m.Main),
    loadChildren: () => import('./modules/main.routes').then((m) => m.MainRoutes),
  },
];
