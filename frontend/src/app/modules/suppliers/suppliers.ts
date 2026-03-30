import { Component, signal } from '@angular/core';
import { SuppliersList } from './suppliers-list/suppliers-list';
import { SuppliersForm } from './suppliers-form/suppliers-form';
import { NgClass } from '@angular/common';

@Component({
  selector: 'app-suppliers',
  imports: [SuppliersList, SuppliersForm, NgClass],
  templateUrl: './suppliers.html',
  styleUrl: './suppliers.css',
})
export class Suppliers {
  public activeTab: 'list' | 'form' = 'list';
  public editingSupplierId: number | null = null;

  showNewForm() {
    this.editingSupplierId = null;
    this.activeTab = 'form';
  }

  editSupplier(id: number) {
    this.editingSupplierId = id;
    this.activeTab = 'form';
  }

  onSaved() {
    this.activeTab = 'list';
    this.editingSupplierId = null;
  }
}
