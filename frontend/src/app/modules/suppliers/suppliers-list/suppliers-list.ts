import { Component, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-suppliers-list',
  imports: [FormsModule],
  templateUrl: './suppliers-list.html',
  styleUrl: './suppliers-list.css',
  standalone: true,
})
export class SuppliersList {
  public edit = output<number>();

  public suppliers = signal<any[]>([]);

  public filterName = signal('');
  public filterTaxId = signal('');
  public filterActive = signal('');

  loadSuppliers() {
    // TODO: cargar proveedores
  }

  onFilterChange() {
    // TODO: implementar filtros
  }

  clearFilters() {
    this.filterName.set('');
    this.filterTaxId.set('');
    this.filterActive.set('');
    this.loadSuppliers();
  }

  deleteSupplier(id: number) {
    if (confirm('¿Estás seguro de eliminar este proveedor?')) {
      // TODO: implementar
    }
  }
}
