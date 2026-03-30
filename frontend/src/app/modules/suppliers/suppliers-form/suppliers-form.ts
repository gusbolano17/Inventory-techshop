import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { SupplierDTO } from '../entities/supplierDTO';
import { email, form, FormField, required } from '@angular/forms/signals';
import { SuppliersService } from '../suppliers.service';

@Component({
  selector: 'app-suppliers-form',
  imports: [FormField],
  templateUrl: './suppliers-form.html',
  styleUrl: './suppliers-form.css',
  standalone: true,
})
export class SuppliersForm implements OnInit {
  public supplierId = input<number | null>(null);
  public saved = output<void>();

  private supplierService = inject(SuppliersService);

  public supplierModel = signal<SupplierDTO>({
    name: '',
    taxId: '',
    email: '',
    phone: '',
    address: '',
    contactName: '',
  });

  public supplierForm = form(this.supplierModel, (path) => {
    required(path.name, { message: 'El nombre es obligatorio' });
    required(path.taxId, { message: 'El identificador fiscal es obligatorio' });
    required(path.email, { message: 'El email es obligatorio' });
    email(path.email, { message: 'Email invalido' });
    required(path.contactName, { message: 'El nombre de contacto es obligatorio' });
  });

  ngOnInit() {
    if (!this.supplierId()) return;
    this.supplierService.obtenerProveedor(this.supplierId()!).subscribe({
      next: (resp) => {
        this.supplierModel.set({
          name: resp.name,
          taxId: resp.taxId,
          email: resp.email,
          phone: resp.phone,
          address: resp.address,
          contactName: resp.contactName,
        });
      },
      error: (err) => console.error('Error cargando proveedor:', err)
    })
  }

  onSubmit(e: Event) {
    e.preventDefault();

    if (this.supplierId()) {
      this.supplierService.editarProveedor(this.supplierId()!, this.supplierModel()).subscribe({
        next: () => this.saved.emit(),
        error: (err) => console.error('Error editando:', err),
      });
    } else {
      this.supplierService.guardarProveedor(this.supplierModel()).subscribe({
        next: () => this.saved.emit(),
        error: (err) => console.error('Error guardando:', err),
      });
    }
  }
}
