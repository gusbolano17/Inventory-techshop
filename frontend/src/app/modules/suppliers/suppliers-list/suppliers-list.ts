import { Component, inject, OnInit, output, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { SupplierModel } from '../entities/SupplierModel';
import { ProveedorFiltroDTO } from '../entities/proveedorFiltroDTO';
import { SuppliersService } from '../suppliers.service';


@Component({
  selector: 'app-suppliers-list',
  imports: [FormField],
  templateUrl: './suppliers-list.html',
  styleUrl: './suppliers-list.css',
  standalone: true,
})
export class SuppliersList implements OnInit {
  public edit = output<number>();

  public suppliers = signal<SupplierModel[]>([]);

  private supplierService = inject(SuppliersService);
  private filterModel = signal<ProveedorFiltroDTO>({
    name: '',
    taxId: '',
    active: true,
    page: 0,
    size: 10,
  });

  public filterForm = form(this.filterModel);

  ngOnInit() {
    this.loadSuppliers();
  }

  loadSuppliers() {
    this.supplierService.listarProveedores().subscribe((resp) => {
      this.suppliers.set(resp);
    });
  }

  onActiveChange(e: Event) {
    e.preventDefault();
    const target = e.target as HTMLSelectElement;
    this.filterModel.update(model => ({
      ...model,
      active: target.value === 'true'
    }));
    this.onFilter(e);
  }

  onFilter(e: Event) {
    e.preventDefault();
    this.supplierService.filtrarProveedores(this.filterModel()).subscribe((resp) => {
      this.suppliers.set(resp.content);
    });
  }

  clearFilters() {
    this.loadSuppliers();
    this.filterModel.set({
      name: '',
      taxId: '',
      active: true,
      page: 0,
      size: 10,
    });
  }

  deleteSupplier(id: number) {
    if (confirm('¿Estás seguro de eliminar este proveedor?')) {
      this.supplierService.eliminarProveedor(id).subscribe({
        next: () => {
          this.loadSuppliers();
        },
        error: (err) => console.error('Error eliminando proveedor:', err)
      })
    }
  }
}
