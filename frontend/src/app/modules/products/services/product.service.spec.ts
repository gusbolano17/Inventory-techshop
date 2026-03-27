import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { of } from 'rxjs';
import { ProductService } from './product.service';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';

describe('ProductService', () => {

  TestBed.configureTestingModule({
    providers: [provideHttpClientTesting()],
  });

  let service: ProductService;
  let mockHttp: {
    get: ReturnType<typeof vi.fn>;
    post: ReturnType<typeof vi.fn>;
    put: ReturnType<typeof vi.fn>;
    delete: ReturnType<typeof vi.fn>;
  };

  const createMockProduct = () => ({
    id: 1,
    name: 'Galaxy S24',
    sku: 'SKU001',
    brand: 'Samsung',
    description: 'Smartphone',
    categoria: 'Electronics',
    minStock: 10,
    salePrice: 800,
    costPrice: 500,
    active: true,
    stock: 50,
    createdAt: '2024-01-01',
    updatedAt: '2024-01-01',
    categoryId: { id: 1, name: 'Electronics', active: true },
    brandId: { id: 1, name: 'Samsung' },
  });

  beforeEach(() => {
    mockHttp = {
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    };

    service = new ProductService(mockHttp as never);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('listarProductos', () => {
    it('should return list of products', () => {
      const mockProducts = [createMockProduct()];
      mockHttp.get.mockReturnValue(of(mockProducts));

      service.listarProductos().subscribe((products) => {
        expect(products.length).toBe(1);
        expect(products[0].name).toBe('Galaxy S24');
      });
    });

    it('should call correct endpoint', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarProductos();

      expect(mockHttp.get).toHaveBeenCalledWith('http://localhost:8090/api/product/listar');
    });
  });

  describe('obtenerProducto', () => {
    it('should return a single product', () => {
      const mockProduct = createMockProduct();
      mockHttp.get.mockReturnValue(of(mockProduct));

      service.obtenerProducto(1).subscribe((product) => {
        expect(product.id).toBe(1);
        expect(product.name).toBe('Galaxy S24');
      });
    });

    it('should call correct endpoint with id', () => {
      mockHttp.get.mockReturnValue(of(createMockProduct()));

      service.obtenerProducto(1);

      expect(mockHttp.get).toHaveBeenCalledWith('http://localhost:8090/api/product/obtener/1');
    });
  });

  describe('guardarProducto', () => {
    it('should create a new product', () => {
      const productDto = {
        name: 'New Product',
        brand: 'Brand',
        description: 'Desc',
        categoria: 'Category',
        sku: 'SKU999',
        minStock: 5,
        salePrice: 100,
        costPrice: 50,
        active: true,
      };

      mockHttp.post.mockReturnValue(of({}));

      service.guardarProducto(productDto).subscribe(() => {
        expect(mockHttp.post).toHaveBeenCalledWith('http://localhost:8090/api/product/agregar', productDto);
      });
    });
  });

  describe('editarProducto', () => {
    it('should update an existing product', () => {
      const productDto = {
        name: 'Updated Product',
        brand: 'Brand',
        description: 'Updated',
        categoria: 'Category',
        sku: 'SKU001',
        minStock: 10,
        salePrice: 900,
        costPrice: 600,
        active: true,
      };

      mockHttp.put.mockReturnValue(of({}));

      service.editarProducto(1, productDto).subscribe(() => {
        expect(mockHttp.put).toHaveBeenCalledWith('http://localhost:8090/api/product/editar/1', productDto);
      });
    });
  });

  describe('eliminarProducto', () => {
    it('should delete a product', () => {
      mockHttp.delete.mockReturnValue(of({}));

      service.eliminarProducto(1).subscribe(() => {
        expect(mockHttp.delete).toHaveBeenCalledWith('http://localhost:8090/api/product/eliminar/1');
      });
    });
  });
});
