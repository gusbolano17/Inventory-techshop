import { Component, signal, Signal } from '@angular/core';
import { NgClass } from '@angular/common';

interface Metrics {
  label : string;
  value : string;
  icon : string;
  color : string;
}

interface InventoryMetric {
  product : string;
  type : string;
  quantity : number;
  date : string;
}

@Component({
  selector: 'app-dashboard',
  imports: [NgClass],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {

  public metrics : Signal<Metrics[]>  = signal([
    { label: 'Ventas Hoy', value: '$12,450', icon: 'trending_up', color: 'teal' },
    { label: 'Ventas Mes', value: '$156,200', icon: 'calendar_month', color: 'blue' },
    { label: 'Usuarios', value: '1,234', icon: 'people', color: 'purple' },
    { label: 'Stock Total', value: '5,678', icon: 'inventory', color: 'amber' }
  ]);

  public movimientos : Signal<InventoryMetric[]> = signal([
    { product: 'Laptop Dell XPS', type: 'entrada', quantity: 50, date: '10:30 AM' },
    { product: 'Mouse Logitech', type: 'salida', quantity: 12, date: '09:15 AM' },
    { product: 'Teclado Mecánico', type: 'entrada', quantity: 100, date: 'Ayer' },
  ]);
}
