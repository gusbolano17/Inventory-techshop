import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { of } from 'rxjs';
import { SuppliersService } from './suppliers.service';

describe('SuppliersService', () => {
  let service: SuppliersService;
  let mockHttp: {
    get: ReturnType<typeof vi.fn>;
    post: ReturnType<typeof vi.fn>;
    put: ReturnType<typeof vi.fn>;
    delete: ReturnType<typeof vi.fn>;
  };

  const createMockSupplier = (id = 1, name = 'Test Supplier', active = true) => ({
    id,
    name,
    taxId: '1234567-8',
    email: 'test@supplier.com',
    phone: '+50212345678',
    address: 'Test Address',
    contactName: 'Contact Person',
    active,
    createdAt: '2024-01-01',
    updatedAt: '2024-01-01',
  });

  beforeEach(() => {
    mockHttp = {
      get: vi.fn(),
      post: vi.fn(),
      put: vi.fn(),
      delete: vi.fn(),
    };

    service = new SuppliersService(mockHttp as never);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('listarProveedores', () => {
    it('should return list of suppliers', () => {
      const mockSuppliers = [
        createMockSupplier(1, 'Supplier 1'),
        createMockSupplier(2, 'Supplier 2'),
      ];

      mockHttp.get.mockReturnValue(of(mockSuppliers));

      service.listarProveedores().subscribe((suppliers) => {
        expect(suppliers.length).toBe(2);
        expect(suppliers[0].name).toBe('Supplier 1');
        expect(suppliers[1].name).toBe('Supplier 2');
      });
    });

    it('should return empty array when no suppliers', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarProveedores().subscribe((suppliers) => {
        expect(suppliers).toEqual([]);
      });
    });

    it('should include inactive suppliers in response', () => {
      const mockSuppliers = [
        createMockSupplier(1, 'Active Supplier', true),
        createMockSupplier(2, 'Inactive Supplier', false),
      ];

      mockHttp.get.mockReturnValue(of(mockSuppliers));

      service.listarProveedores().subscribe((suppliers) => {
        expect(suppliers.length).toBe(2);
        expect(suppliers[1].active).toBe(false);
      });
    });

    it('should call correct endpoint', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarProveedores();

      expect(mockHttp.get).toHaveBeenCalledWith('http://localhost:8090/api/suppliers/listar');
    });
  });

  describe('obtenerProveedor', () => {
    it('should return a single supplier', () => {
      const mockSupplier = createMockSupplier(1, 'Test Supplier');
      mockHttp.get.mockReturnValue(of(mockSupplier));

      service.obtenerProveedor(1).subscribe((supplier) => {
        expect(supplier.id).toBe(1);
        expect(supplier.name).toBe('Test Supplier');
      });
    });

    it('should call correct endpoint with id', () => {
      mockHttp.get.mockReturnValue(of(createMockSupplier()));

      service.obtenerProveedor(1);

      expect(mockHttp.get).toHaveBeenCalledWith('http://localhost:8090/api/suppliers/obtener/1');
    });
  });

  describe('guardarProveedor', () => {
    it('should create a new supplier', () => {
      const supplierData = {
        name: 'New Supplier',
        taxId: '9999999-9',
        email: 'new@supplier.com',
        phone: '+50299999999',
        address: 'New Address',
        contactName: 'New Contact',
      };

      mockHttp.post.mockReturnValue(of({}));

      service.guardarProveedor(supplierData).subscribe(() => {
        expect(mockHttp.post).toHaveBeenCalledWith(
          'http://localhost:8090/api/suppliers/agregar',
          supplierData
        );
      });
    });

    it('should send correct payload structure', () => {
      mockHttp.post.mockReturnValue(of({}));

      service.guardarProveedor({
        name: 'Test',
        taxId: '123',
        email: 'test@test.com',
        phone: '123',
        address: 'Address',
        contactName: 'Contact',
      });

      expect(mockHttp.post).toHaveBeenCalledWith(
        'http://localhost:8090/api/suppliers/agregar',
        {
          name: 'Test',
          taxId: '123',
          email: 'test@test.com',
          phone: '123',
          address: 'Address',
          contactName: 'Contact',
        }
      );
    });
  });

  describe('editarProveedor', () => {
    it('should update an existing supplier', () => {
      const supplierData = {
        name: 'Updated Supplier',
        taxId: '1111111-1',
        email: 'updated@supplier.com',
        phone: '+50211111111',
        address: 'Updated Address',
        contactName: 'Updated Contact',
      };

      mockHttp.put.mockReturnValue(of({}));

      service.editarProveedor(1, supplierData).subscribe(() => {
        expect(mockHttp.put).toHaveBeenCalledWith(
          'http://localhost:8090/api/suppliers/editar/1',
          supplierData
        );
      });
    });

    it('should include id in URL path', () => {
      mockHttp.put.mockReturnValue(of({}));

      service.editarProveedor(5, {
        name: 'Test',
        taxId: '123',
        email: 'test@test.com',
        phone: '123',
        address: 'Address',
        contactName: 'Contact',
      });

      expect(mockHttp.put).toHaveBeenCalledWith(
        'http://localhost:8090/api/suppliers/editar/5',
        {
          name: 'Test',
          taxId: '123',
          email: 'test@test.com',
          phone: '123',
          address: 'Address',
          contactName: 'Contact',
        }
      );
    });
  });

  describe('eliminarProveedor', () => {
    it('should delete a supplier', () => {
      mockHttp.delete.mockReturnValue(of({}));

      service.eliminarProveedor(1).subscribe(() => {
        expect(mockHttp.delete).toHaveBeenCalledWith(
          'http://localhost:8090/api/suppliers/eliminar/1'
        );
      });
    });

    it('should include id in URL path', () => {
      mockHttp.delete.mockReturnValue(of({}));

      service.eliminarProveedor(10);

      expect(mockHttp.delete).toHaveBeenCalledWith(
        'http://localhost:8090/api/suppliers/eliminar/10'
      );
    });
  });
});
