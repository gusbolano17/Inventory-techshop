import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest';
import { of } from 'rxjs';
import { BrandService } from './brand.service';
import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting } from '@angular/common/http/testing';


describe('BrandService', () => {

  TestBed.configureTestingModule({
    providers: [provideHttpClientTesting()],
  });

  let service: BrandService;
  let mockHttp: {
    get: ReturnType<typeof vi.fn>;
  };

  beforeEach(() => {
    mockHttp = {
      get: vi.fn(),
    };

    service = new BrandService(mockHttp as never);
  });

  afterEach(() => {
    vi.clearAllMocks();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('listarMarcas', () => {
    it('should return list of brands', () => {
      const mockBrands = [
        { id: 1, name: 'Samsung' },
        { id: 2, name: 'Apple' },
      ];

      mockHttp.get.mockReturnValue(of(mockBrands));

      service.listarMarcas().subscribe((brands) => {
        expect(brands.length).toBe(2);
        expect(brands[0].name).toBe('Samsung');
        expect(brands[1].name).toBe('Apple');
      });
    });

    it('should return empty array when no brands', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarMarcas().subscribe((brands) => {
        expect(brands).toEqual([]);
      });
    });

    it('should call correct endpoint', () => {
      mockHttp.get.mockReturnValue(of([]));

      service.listarMarcas();

      expect(mockHttp.get).toHaveBeenCalledWith('http://localhost:8090/api/brands/listar');
    });
  });
});
