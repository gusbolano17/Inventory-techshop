export interface Category {
  id: number;
  name: string;
  description: string;
  active: boolean;
  createdAt?: string;
}

export type CategoryDto = Omit<Category, "id" | "active">;
