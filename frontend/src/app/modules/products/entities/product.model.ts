import { Category } from '../../categories/category';
import { Brand } from './brand.model';
import { ProductDto } from './ProductDto';

export interface ProductModel extends ProductDto {
  id: number;
  stock: number;
  active: boolean;
  createdAt: string; // LocalDateTime se serializa como string
  updatedAt: string;
  categoryId: Category;
  brandId: Brand;
}
