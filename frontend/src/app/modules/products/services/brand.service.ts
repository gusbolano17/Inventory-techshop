import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Environment } from '../../../core/Environment';
import { Brand } from '../entities/brand.model';

@Injectable({
  providedIn: 'root',
})
export class BrandService {

  private http: HttpClient = inject(HttpClient);

  listarMarcas(): Observable<Brand[]> {
    return this.http.get<Brand[]>(`${Environment.API_URL}/brands/listar`);
  }
}
