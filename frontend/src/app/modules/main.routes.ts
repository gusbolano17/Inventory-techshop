import { Routes } from '@angular/router';

export const MainRoutes : Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent : () => import('./dashboard/dashboard').then(m => m.Dashboard)
  },
  {
    path: 'products',
    loadChildren: () => import('./products/product.routes').then(m => m.ProductRoutes)
  },
  {
    path: 'categories',
    loadComponent: () => import('./categories/categories').then(m => m.Categories)
  },
  {
    path: 'suppliers',
    loadComponent: () => import('./suppliers/suppliers').then(m => m.Suppliers)
  }
];
