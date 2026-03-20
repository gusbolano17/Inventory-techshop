import { Component, input, InputSignal, output } from '@angular/core';
import { NgClass } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MenuItem } from '../../modules/main/main';


@Component({
  selector: 'Sidenav',
  imports: [NgClass, RouterLink, RouterLinkActive],
  templateUrl: './sidenav.html',
  styleUrl: './sidenav.css',
})
export class Sidenav {
  public collapsed: InputSignal<boolean> = input(false);
  public menuItems: InputSignal<MenuItem[]> = input<MenuItem[]>([]);
  public closedSidenav = output<void>();
}
