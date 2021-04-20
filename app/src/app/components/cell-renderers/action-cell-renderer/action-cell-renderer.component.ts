import { Component, Output } from '@angular/core';
import { ICellRendererAngularComp } from 'ag-grid-angular';
import { ICellRendererParams } from 'ag-grid-community';

@Component({
  selector: 'app-action-cell-renderer',
  templateUrl: './action-cell-renderer.component.html',
  styleUrls: ['./action-cell-renderer.component.scss']
})
export class ActionCellRendererComponent implements ICellRendererAngularComp {

  private parent: any;
  private params: ICellRendererParams | undefined;
  isEditing: boolean = false;

  agInit(params: ICellRendererParams): void {
    this.parent = params.context.componentParent;
    this.params = params;
  }

  refresh(params: ICellRendererParams): boolean {
    let isCurrentRowEditing = params.api.getEditingCells().some((cell: any) => {
      return cell.rowIndex === params.node.rowIndex;
    });

    this.isEditing = isCurrentRowEditing;
    return isCurrentRowEditing;
  }

  edit(action: string): void {
    if (!this.params) {
      return;
    }

    if (this.params.column.getColId() === 'action' && action) {
      let rowIndex = this.params.node.rowIndex;

      if (rowIndex === undefined || rowIndex === null) {
        rowIndex = -1;
      }

      switch (action) {
        case 'edit':
          this.parent.edit(this.params.node.data);
          break;
        case 'delete':
          this.parent.delete(this.params.node.data.id);
          break;
        case 'update':
          this.isEditing = false;
          this.parent.update(this.params.data);
          break;
        case 'cancel':
          this.isEditing = false;
          this.params.api.stopEditing(false);
          break;
      }
    }
  }
}

