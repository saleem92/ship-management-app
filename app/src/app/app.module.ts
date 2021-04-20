import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AgGridModule } from 'ag-grid-angular';

import { AppComponent } from './components/app.component';
import { ShipListComponent } from './components/manage-ships/ship-list/ship-list.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { HeaderComponent } from './components/header/header.component';

import { MatIconModule } from '@angular/material/icon';

import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ActionCellRendererComponent } from './components/cell-renderers/action-cell-renderer/action-cell-renderer.component';
import { SearchBoxComponent } from './components/search-box/search-box.component';
import { NewShipDialogComponent } from './components/manage-ships/new-ship-dialog/new-ship-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    ShipListComponent,
    HeaderComponent,
    ActionCellRendererComponent,
    SearchBoxComponent,
    NewShipDialogComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AgGridModule.withComponents([]),
    NoopAnimationsModule,
    ReactiveFormsModule,

    // Material
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,

    AppRoutingModule
  ],
  entryComponents: [NewShipDialogComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
