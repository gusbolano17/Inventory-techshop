import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { of } from 'rxjs';
import { CategoryService } from './category.service';

describe('CategoryService', () => {
  let service: CategoryService;
  let mockHttp: {
    get: ReturnType<typeof vi.fn>;
    post: ReturnType<typeof vi.fn>;
    put: ReturnType<typeof vi.fn>;
    delete: ReturnType<typeof vi.fn>;
  };

  const createMockCategory = (id = 1, name = 'Electronics', active = true, description = 'Description') => ({
    id,
    name,
    active,
    description,
  });

  beforeEach(() => {
    mockHttp = {
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    };

    service = new CategoryService(mockHttp as never);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('listarCategorias', () => {
    it('should return list of categories', () => {
      const mockCategories = [
        createMockCategory(1,'Electronics', true ,'Electronics Description'),
        createMockCategory(2,'Clothing', true ,'Clothing Description'),
      ];

      mockHttp.get.mockReturnValue(of(mockCategories));

      service.listarCategorias().subscribe((categories) => {
        expect(categories.length).toBe(2);
        expect(categories[0].name).toBe('Electronics');
        expect(categories[1].name).toBe('Clothing');
      });
    });

    it('should return empty array when no categories', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarCategorias().subscribe((categories) => {
        expect(categories).toEqual([]);
      });
    });

    it('should include inactive categories in response', () => {
      const mockCategories = [
        createMockCategory(1, 'Active Category', true),
        createMockCategory(2, 'Inactive Category', false),
      ];

      mockHttp.get.mockReturnValue(of(mockCategories));

      service.listarCategorias().subscribe((categories) => {
        expect(categories.length).toBe(2);
        expect(categories[1].active).toBe(false);
      });
    });

    it('should call correct endpoint', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarCategorias();

      expect(mockHttp.get).toHaveBeenCalledWith('http://localhost:8090/api/categories/listar');
    });
  });

  describe('guardarCategoria', () => {
    it('should create a new category', () => {
      const categoryData = { name: 'New Category', description: 'Desc' };
      const mockResponse = { message: 'Categoria creada exitosamente' };

      mockHttp.post.mockReturnValue(of(mockResponse));

      service.guardarCategoria(categoryData).subscribe((response) => {
        expect(response).toEqual(mockResponse);
        expect(mockHttp.post).toHaveBeenCalledWith(
          'http://localhost:8090/api/categories/guardar',
          categoryData
        );
      });
    });

    it('should send correct payload structure', () => {
      mockHttp.post.mockReturnValue(of({ message: 'ok' }));

      service.guardarCategoria({ name: 'Test', description: 'Desc' });

      expect(mockHttp.post).toHaveBeenCalledWith(
        'http://localhost:8090/api/categories/guardar',
        { name: 'Test', description: 'Desc' }
      );
    });
  });

  describe('editarCategoria', () => {
    it('should update an existing category', () => {
      const categoryId = 1;
      const categoryData = { name: 'Updated Category', description: 'Desc' };
      const mockResponse = { message: 'Categoria editada exitosamente' };

      mockHttp.put.mockReturnValue(of(mockResponse));

      service.editarCategoria(categoryId, categoryData).subscribe((response) => {
        expect(response).toEqual(mockResponse);
        expect(mockHttp.put).toHaveBeenCalledWith(
          'http://localhost:8090/api/categories/actualizar/1',
          categoryData
        );
      });
    });

    it('should include id in URL path', () => {
      mockHttp.put.mockReturnValue(of({ message: 'ok' }));

      service.editarCategoria(5, { name: 'Updated', description: 'Desc' });

      expect(mockHttp.put).toHaveBeenCalledWith(
        'http://localhost:8090/api/categories/actualizar/5',
        { name: 'Updated', description: 'Desc' }
      );
    });
  });

  describe('eliminarCategoria', () => {
    it('should delete a category', () => {
      const categoryId = 1;
      const mockResponse = { message: 'Categoria eliminada exitosamente' };

      mockHttp.delete.mockReturnValue(of(mockResponse));

      service.eliminarCategoria(categoryId).subscribe((response) => {
        expect(response).toEqual(mockResponse);
        expect(mockHttp.delete).toHaveBeenCalledWith(
          'http://localhost:8090/api/categories/eliminar/1'
        );
      });
    });

    it('should include id in URL path', () => {
      mockHttp.delete.mockReturnValue(of({}));

      service.eliminarCategoria(10);

      expect(mockHttp.delete).toHaveBeenCalledWith(
        'http://localhost:8090/api/categories/eliminar/10'
      );
    });
  });
});
