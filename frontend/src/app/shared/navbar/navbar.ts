import { Component, output, signal, computed } from '@angular/core';

@Component({
  selector: 'Navbar',
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  public toggleCollapse = output<void>();

  public currentDate = signal(new Date());
  public currentPage = signal('Dashboard');
  public showUserMenu = signal(false);
  public notificationCount = signal(3);

  public formattedDate = computed(() => {
    const date = this.currentDate();
    return date.toLocaleDateString('es-ES', {
      day: 'numeric',
      month: 'short',
      year: 'numeric'
    }).replace(/\./g, '');
  });

  public user = {
    name: 'Usuario',
    email: 'usuario@techman.com',
    initials: 'U'
  };

  toggleUserMenu() {
    this.showUserMenu.update(v => !v);
  }
}
