import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from './category';
import { Environment } from '../../core/Environment';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {

  constructor(private http: HttpClient) {}

  listarCategorias(): Observable<Category[]> {
    return this.http.get<Category[]>(`${Environment.API_URL}/categories/listar`);
  }
}
