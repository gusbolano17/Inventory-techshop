import { Routes } from '@angular/router';

export const ProductRoutes : Routes = [
  {
    path : '',
    loadComponent : () => import('./products').then(m => m.Products)
  }
];
