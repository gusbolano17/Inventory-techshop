import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category, CategoryDto } from './category';
import { Environment } from '../../core/Environment';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  listarCategorias(): Observable<Category[]> {
    return this.http.get<Category[]>(`${Environment.API_URL}/categories/listar`);
  }

  guardarCategoria(body: CategoryDto): Observable<any> {
    return this.http.post(`${Environment.API_URL}/categories/guardar`, body);
  }

  editarCategoria(id: number, body: CategoryDto): Observable<any> {
    return this.http.put(`${Environment.API_URL}/categories/actualizar/${id}`, body);
  }

  eliminarCategoria(id : number) : Observable<any>{
    return this.http.delete(`${Environment.API_URL}/categories/eliminar/${id}`);
  }
}
