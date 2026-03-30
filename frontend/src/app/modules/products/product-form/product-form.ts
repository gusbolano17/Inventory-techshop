import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { ProductDto } from '../entities/ProductDto';
import { form, FormField, required } from '@angular/forms/signals';
import { Category } from '../../categories/category';
import { Brand } from '../entities/brand.model';
import { ProductService } from '../services/product.service';
import { ProductModel } from '../entities/product.model';

@Component({
  selector: 'app-product-form',
  imports: [FormField],
  templateUrl: './product-form.html',
  styleUrl: './product-form.css',
})
export class ProductForm implements OnInit {
  public productId = input<number | null>(null);
  public categorias = input<Category[]>([]);
  public brands = input<Brand[]>([]);

  public saved = output<void>();

  private productService = inject(ProductService);
  
  public currentStock = signal<number>(0);

  public productModel = signal<ProductDto>({
    name: '',
    brand: '',
    description: '',
    categoria: '',
    sku: '',
    salePrice: 0,
    costPrice: 0,
    minStock: 0,
    active: true,
  });

  public productForm = form(this.productModel, (path) => {
    required(path.name, { message: 'El nombre es obligatorio' });
    required(path.brand, { message: 'La marca es obligatoria' });
    required(path.description, { message: 'La descripción es obligatoria' });
    required(path.categoria, { message: 'La categoría es obligatoria' });
    required(path.sku, { message: 'El SKU es obligatorio' });
  });

  ngOnInit() {
    if (!this.productId()) return;
    
    this.productService.obtenerProducto(this.productId()!).subscribe({
      next: (resp: ProductModel) => {
        this.productModel.set({
          name: resp.name,
          brand: resp.brandId?.name || resp.brand || '',
          description: resp.description || '',
          categoria: resp.categoryId?.name || resp.categoria || '',
          sku: resp.sku,
          salePrice: resp.salePrice,
          costPrice: resp.costPrice,
          minStock: resp.minStock || 0,
          active: resp.active,
        });
        this.currentStock.set(resp.stock || 0);
      },
      error: (err) => {
        console.error('Error cargando producto:', err);
      }
    });
  }

  onActiveChange(event: Event) {
    const target = event.target as HTMLSelectElement;
    this.productModel.update(model => ({
      ...model,
      active: target.value === 'true'
    }));
  }

  onSubmit(e: Event) {
    e.preventDefault();
    
    if (this.productId()) {
      this.productService.editarProducto(this.productId()!, this.productModel()).subscribe({
        next: () => this.saved.emit(),
        error: (err) => console.error('Error editando:', err)
      });
    } else {
      this.productService.guardarProducto(this.productModel()).subscribe({
        next: () => this.saved.emit(),
        error: (err) => console.error('Error guardando:', err)
      });
    }
  }
}
