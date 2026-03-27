import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Environment } from '../../../core/Environment';
import { Brand } from '../entities/brand.model';

@Injectable({
  providedIn: 'root',
})
export class BrandService {

  constructor(private http: HttpClient) {}

  listarMarcas(): Observable<Brand[]> {
    return this.http.get<Brand[]>(`${Environment.API_URL}/brands/listar`);
  }
}
