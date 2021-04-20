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
          console.log('quick', params)
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

  ngOnChanges(changes: SimpleChanges): void {
    if (!this.gridOptions?.api) {
      return;
    }

    const { searchText: { previousValue, currentValue } } = changes;

    console.log(previousValue, currentValue, 'current value')

    if (previousValue !== currentValue) {
      this.gridOptions.api.setQuickFilter(currentValue);
    }
  }

  ngOnInit(): void {
    this.appService.getShips().subscribe((res: Ship[]) => {
      this.rowData = res
    });
  }

  onRowEditingStarted(params: any) {
    params.api.refreshCells({
      columns: ['action'],
      rowNodes: [params.node],
      force: true
    });
  }

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

  update(data: Ship) {
    this.appService.updateShipDetails(data).subscribe(res => {
      const index = this.rowData.findIndex(x => x.id === data.id);
      this.rowData = Object.assign([], this.rowData, { [index]: res });

      alert('Data updated successfully');
    });
  }

  delete(id: number) {
    this.appService.deleteShip(id).subscribe(_ => {
      this.rowData = [...this.rowData.filter(x => x.id !== id)];

      alert("Data deleted successfully");
    });
  }

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
