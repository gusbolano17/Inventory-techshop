import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { of } from 'rxjs';
import { CategoryService } from './category.service';

describe('CategoryService', () => {
  let service: CategoryService;
  let mockHttp: {
    get: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    mockHttp = {
      get: vi.fn(),
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
        { id: 1, name: 'Electronics', active: true },
        { id: 2, name: 'Clothing', active: true },
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
        { id: 1, name: 'Active Category', active: true },
        { id: 2, name: 'Inactive Category', active: false },
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
});
