import { Component, inject, input, OnInit, output, signal } from '@angular/core';
import { ProductoFiltroDTO } from '../entities/productoFiltroDTO';
import { form, FormField } from '@angular/forms/signals';
import { ProductService } from '../services/product.service';
import { ProductModel } from '../entities/product.model';
import { Category } from '../../categories/category';
import { Brand } from '../entities/brand.model';

@Component({
  selector: 'app-product-list',
  imports: [FormField],
  templateUrl: './product-list.html',
  styleUrl: './product-list.css',
})
export class ProductList implements OnInit {
  public categorias = input<Category[]>([]);
  public brands = input<Brand[]>([]);
  public edit = output<number>();

  private productService = inject(ProductService);

  public products = signal<ProductModel[]>([]);
  private filterModel = signal<ProductoFiltroDTO>({
    name: '',
    brand: '',
    sku: '',
    category: '',
    page: 0,
    size: 10,
  });

  public filterForm = form(this.filterModel);

  ngOnInit() {
    this.productService.listarProductos().subscribe((resp) => {
      this.products.set(resp);
    });
  }

  onFilter(e: Event) {
    e.preventDefault();
    this.productService.filtrarProductos(this.filterModel()).subscribe((resp) => {
      this.products.set(resp.content);
    });
  }

  deleteProduct(id : number){
    if(window.confirm('Estas seguro de eliminar este producto?')){
      this.productService.eliminarProducto(id).subscribe({
        next: () => {
          this.products.update(products => products.filter(product => product.id !== id));
        },
        error: (err) => console.error('Error eliminando producto:', err)
      });
    }
  }
}
