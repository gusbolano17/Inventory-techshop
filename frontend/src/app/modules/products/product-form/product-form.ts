import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { ProductDto } from '../entities/ProductDto';
import { form, FormField, required } from '@angular/forms/signals';
import { Category } from '../../categories/category';
import { Brand } from '../entities/brand.model';
import { ProductService } from '../services/product.service';

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

  private productModel = signal<ProductDto>({
    name: '',
    brand: '',
    description: '',
    categoria: '',
    sku: '',
    salePrice: 0,
    costPrice: 0,
    minStock : 0,
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
    this.productService.obtenerProducto(this.productId()).subscribe((resp) => {
      this.productModel.set({
        ...resp,
        categoria : resp.categoryId.name,
        brand : resp.brandId.name
      })
    })
  }
  onSubmit(e: Event) {
    e.preventDefault();
    if (this.productId()){
      this.productService.editarProducto(this.productId()!, this.productModel()).subscribe({
        next: () => {
          this.saved.emit();
        },
      })
    }else{
      this.productService.guardarProducto(this.productModel()).subscribe({
        next: () => {
          this.saved.emit();
        },
      });
    }
  }
}
