import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProductDto } from '../entities/ProductDto';
import { Environment } from '../../../core/Environment';
import { ProductoFiltroDTO } from '../entities/productoFiltroDTO';
import { FiltroResponse } from '../../../shared/filtroResponse';
import { ProductModel } from '../entities/product.model';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private http: HttpClient = inject(HttpClient);
  private baseUrl = `${Environment.API_URL}/product`;

  listarProductos(): Observable<ProductModel[]> {
    return this.http.get<ProductModel[]>(`${this.baseUrl}/listar`);
  }

  obtenerProducto(id: number | null): Observable<ProductModel> {
    return this.http.get<ProductModel>(`${this.baseUrl}/obtener/${id}`);
  }

  filtrarProductos(filtros: ProductoFiltroDTO): Observable<FiltroResponse<ProductModel>> {
    let params = new HttpParams();

    Object.keys(filtros).forEach((key) => {
      const typedKey = key as keyof ProductoFiltroDTO;
      if (
        filtros[typedKey] !== null &&
        filtros[typedKey] !== '' &&
        filtros[typedKey] !== undefined
      ) {
        params = params.set(key, String(filtros[typedKey]));
      }
    });

    return this.http.get<FiltroResponse<ProductModel>>(`${this.baseUrl}/filtrar`, { params });
  }

  guardarProducto(body: ProductDto): Observable<any> {
    return this.http.post(`${this.baseUrl}/agregar`, body);
  }

  editarProducto(id: number, body: ProductDto): Observable<any> {
    return this.http.put(`${this.baseUrl}/editar/${id}`, body);
  }

  eliminarProducto(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/eliminar/${id}`);
  }
}
