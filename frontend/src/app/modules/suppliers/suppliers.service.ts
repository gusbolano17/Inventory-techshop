import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Environment } from '../../core/Environment';
import { Observable } from 'rxjs';
import { FiltroResponse } from '../../shared/filtroResponse';
import { SupplierModel } from './entities/SupplierModel';
import { SupplierDTO } from './entities/supplierDTO';
import { ProveedorFiltroDTO } from './entities/proveedorFiltroDTO';

@Injectable({
  providedIn: 'root',
})
export class SuppliersService {
  private http: HttpClient;
  private baseUrl = `${Environment.API_URL}/suppliers`;

  constructor(http: HttpClient) {
    this.http = http;
  }

  listarProveedores(): Observable<SupplierModel[]> {
    return this.http.get<SupplierModel[]>(`${this.baseUrl}/listar`);
  }

  obtenerProveedor(id: number | null): Observable<SupplierModel> {
    return this.http.get<SupplierModel>(`${this.baseUrl}/obtener/${id}`);
  }

  filtrarProveedores(filtros: ProveedorFiltroDTO): Observable<FiltroResponse<SupplierModel>> {
    let params = new HttpParams();

    Object.keys(filtros).forEach((key) => {
      const typedKey = key as keyof ProveedorFiltroDTO;
      if (
        filtros[typedKey] !== null &&
        filtros[typedKey] !== '' &&
        filtros[typedKey] !== undefined
      ) {
        params = params.set(key, String(filtros[typedKey]));
      }
    });

    return this.http.get<FiltroResponse<SupplierModel>>(`${this.baseUrl}/filtrar`, { params });
  }

  guardarProveedor(body: SupplierDTO): Observable<any> {
    return this.http.post(`${this.baseUrl}/guardar`, body);
  }

  editarProveedor(id: number, body: SupplierDTO): Observable<any> {
    return this.http.put(`${this.baseUrl}/actualizar/${id}`, body);
  }

  eliminarProveedor(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/eliminar/${id}`);
  }
}
