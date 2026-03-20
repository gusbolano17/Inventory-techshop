import { Component, signal, WritableSignal, HostListener } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgClass } from '@angular/common';
import { Navbar } from '../../shared/navbar/navbar';
import { Sidenav } from '../../shared/sidenav/sidenav';

export interface MenuItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'app-main',
  imports: [RouterOutlet, Sidenav, Navbar, NgClass],
  templateUrl: './main.html',
  styleUrl: './main.css',
})
export class Main {
  public collapse: WritableSignal<boolean> = signal(false);

  public menuItems: MenuItem[] = [
    { label: 'Dashboard', icon: 'dashboard', route: '/main/dashboard' },
    { label: 'Productos', icon: 'inventory', route: '/main/products' },
    { label: 'Categorías', icon: 'category', route: '/main/categories' },
    { label: 'Proveedores', icon: 'local_shipping', route: '/main/suppliers' },
    { label: 'Usuarios', icon: 'people', route: '/main/users' },
    { label: 'Reportes', icon: 'assessment', route: '/main/reports' },
    { label: 'Configuración', icon: 'settings', route: '/main/settings' },
  ];

  @HostListener('window:keydown.escape')
  onEscapeKey() {
    if (!this.collapse()) {
      this.toggleCollapse();
    }
  }

  toggleCollapse() {
    this.collapse.update((v) => !v);
  }
}
