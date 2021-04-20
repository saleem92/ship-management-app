import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { GridOptions } from 'ag-grid-community';
import { Ship } from 'src/app/models/Ship';
import { AppService } from 'src/app/services/app.service';
import { NewShipDialogComponent } from '../new-ship-dialog/new-ship-dialog.component';
import { ActionCellRendererComponent } from '../../cell-renderers/action-cell-renderer/action-cell-renderer.component';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-ship-list',
  templateUrl: './ship-list.component.html',
  styleUrls: ['./ship-list.component.scss']
})
export class ShipListComponent implements OnInit, OnChanges {

  @Input('searchText') searchText: string | undefined;

  gridOptions: GridOptions = {
    columnDefs: [
      {
        field: 'id',
        filter: true,
        getQuickFilterText: params => {
          return params.data.id;
        }
      },
      {
        field: 'name',
        filter: 'agTextColumnFilter',
        getQuickFilterText: params => {
          return params.data.name;
        }
      },
      {
        field: 'code',
        filter: 'agTextColumnFilter',
        getQuickFilterText: params => {
          return params.data.code;
        }
      },
      {
        headerName: 'Length (m)',
        field: 'length',
        filter: 'agNumberColumnFilter',
        getQuickFilterText: params => {
          return params.data.length;
        }
      },
      {
        headerName: 'Width (m)',
        field: 'width',
        filter: 'agNumberColumnFilter',
        getQuickFilterText: params => {
          return params.data.width;
        }
      },
      {
        headerName: 'Actions',
        minWidth: 150,
        cellRenderer: 'actionCellRenderer',
        editable: false,
        colId: 'action'
      }
    ],
    frameworkComponents: {
      actionCellRenderer: ActionCellRendererComponent
    },
    defaultColDef: {
      sortable: true
    },
    context: {
      componentParent: this
    }
  };

  rowData: Ship[] = [];

  constructor(private appService: AppService,
    private dialog: MatDialog) { }

  /**
   * Filters text globally in the grid irrespective of column
   * @param changes angular changes
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (!this.gridOptions?.api) {
      return;
    }

    const { searchText: { previousValue, currentValue } } = changes;

    if (previousValue !== currentValue) {
      this.gridOptions.api.setQuickFilter(currentValue);
    }
  }

  /**
   * fetches data from backend and updates the table rows
   */
  ngOnInit(): void {
    this.appService.getShips().subscribe((res: Ship[]) => {
      this.rowData = res
    });
  }

  /**
   * Opens the dialog with previously saved state set to it
   * @param data the data to update
   */
  edit(data: Ship) {
    const dialogRef = this.dialog.open(NewShipDialogComponent, {
      data: data
    });

    dialogRef.afterClosed().subscribe(
      (result: Ship) => {
        if (result) {
          this.appService.updateShipDetails(result).subscribe(
            (res: Ship) => {
              const data = this.rowData.filter(x => x.id != res.id);
              this.rowData = [...data, res];

              alert("Updated Successfully");
            },
            (err: HttpErrorResponse) => {
              alert(err.error.message);
            }
          );
        }
      }
    );
  }

  /**
   * Patches ships when there's an update
   * @param data 
   */
  update(data: Ship) {
    this.appService.updateShipDetails(data).subscribe(res => {
      const index = this.rowData.findIndex(x => x.id === data.id);
      this.rowData = Object.assign([], this.rowData, { [index]: res });

      alert('Data updated successfully');
    });
  }

  /**
   * Deletes an item from list of ships
   * @param id id of the resource
   */
  delete(id: number) {
    this.appService.deleteShip(id).subscribe(_ => {
      this.rowData = [...this.rowData.filter(x => x.id !== id)];

      alert("Data deleted successfully");
    });
  }

  /**
   * Open Dialog when there's a new ship to be created
   */
  openDialog(): void {
    const dialogRef = this.dialog.open(NewShipDialogComponent);

    dialogRef.afterClosed().subscribe(
      (result: Ship) => {
        if (result) {
          this.appService.createShip(result).subscribe(
            (res: any) => {
              this.rowData = [...this.rowData, res];

              alert("Added Successfully");
            },
            (err: HttpErrorResponse) => {
              alert(err.error.message);
            }
          );
        }
      }
    );
  }
}
