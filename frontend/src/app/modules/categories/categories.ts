import {
  Component,
  ElementRef,
  inject,
  OnInit,
  signal,
  ViewChild,
} from '@angular/core';
import { CategoryService } from './category.service';
import { Category, CategoryDto } from './category';
import { form, FormField, required } from '@angular/forms/signals';

@Component({
  selector: 'app-categories',
  imports: [FormField],
  templateUrl: './categories.html',
  styleUrl: './categories.css',
})
export class Categories implements OnInit {
  @ViewChild('dialog') dialogElement!: ElementRef<HTMLDialogElement>;

  private categoryService = inject(CategoryService);

  public categories = signal<Category[]>([]);
  public editingCategory = signal<Category | null>(null);

  public categoryModel = signal<CategoryDto>({
    name: '',
    description: '',
  });

  public categoryForm = form(this.categoryModel, (p) =>{
    required(p.name, { message: 'El nombre es obligatorio' })
  })

  ngOnInit() {
    this.loadCategories();
  }

  loadCategories() {
    this.categoryService.listarCategorias().subscribe({
      next: (data) => this.categories.set(data),
      error: (err) => console.error('Error cargando categorias:', err),
    });
  }

  openNewModal() {
    this.editingCategory.set(null);
    this.dialogElement.nativeElement.showModal();
  }

  openEditModal(category: Category) {
    this.editingCategory.set(category);
    this.dialogElement.nativeElement.showModal();
  }

  closeModal() {
    this.dialogElement.nativeElement.close();
  }

  saveCategory(e: Event) {
    e.preventDefault();
    const postService$ =
      this.editingCategory() ? this.categoryService.editarCategoria(this.editingCategory()!.id, this.categoryModel())
        : this.categoryService.guardarCategoria(this.categoryModel());

    postService$.subscribe({
      next: () => {
        this.closeModal();
        this.loadCategories();
      },
      error: (err) => console.error('Error guardando categoria:', err),
    });
  }

  deleteCategory(id: number) {
    if (confirm('¿Estás seguro de eliminar esta categoría?')) {
      this.categoryService.eliminarCategoria(id).subscribe({
        next: () => {
          this.loadCategories();
        },
        error: (err) => console.error('Error eliminando categoria:', err)
      })
    }
  }
}
