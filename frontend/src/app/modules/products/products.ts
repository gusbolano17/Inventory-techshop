import { Component, inject, OnInit, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { ProductList } from './product-list/product-list';
import { ProductForm } from './product-form/product-form';
import { CategoryService } from '../categories/category.service';
import { BrandService } from './services/brand.service';
import { Category } from '../categories/category';
import { Brand } from './entities/brand.model';

@Component({
  selector: 'app-products',
  imports: [ProductList, ProductForm, NgClass],
  templateUrl: './products.html',
  styleUrl: './products.css',
})
export class Products implements OnInit {
  public activeTab: 'list' | 'form' = 'list';
  public editingProductId: number | null = null;
  public categorias = signal<Category[]>([]);
  public brands = signal<Brand[]>([]);

  private categoryService = inject(CategoryService);
  private brandService = inject(BrandService);

  ngOnInit() {
    this.categoryService.listarCategorias().subscribe((categorias) => {
      this.categorias.set(categorias);
    });
    this.brandService.listarMarcas().subscribe((brands) => {
      this.brands.set(brands);
    });
  }

  showNewForm() {
    this.editingProductId = null;
    this.activeTab = 'form';
  }

  editProduct(id: number) {
    this.editingProductId = id;
    this.activeTab = 'form';
  }

  onSaved() {
    this.activeTab = 'list';
    this.editingProductId = null;
  }
}
