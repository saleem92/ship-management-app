import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatIconModule } from '@angular/material/icon';

import { ActionCellRendererComponent } from './action-cell-renderer.component';

describe('ActionCellRendererComponent', () => {
  let component: ActionCellRendererComponent;
  let fixture: ComponentFixture<ActionCellRendererComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ActionCellRendererComponent],
      imports: [MatIconModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ActionCellRendererComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should return true if current row is being edited', () => {
    const params: any = {
      node: { rowIndex: 1 },
      value: 'test', api: {
        getEditingCells: () => { return [{ rowIndex: 1 }] }
      }
    };

    expect(component.refresh(params)).toBeTruthy();
    expect(component.isEditing).toBeTruthy();
  });

  it('should false if current row is not being edited', () => {
    const params: any = {
      node: { rowIndex: 2 },
      value: 'test', api: {
        getEditingCells: () => { return [{ rowIndex: 1 }] }
      }
    };

    expect(component.refresh(params)).toBeFalsy();
    expect(component.isEditing).toBeFalsy();
  });

  it('should call parent\'s edit method when edit is clicked', () => {
    const params: any = {
      context: {
        componentParent: { edit: (data: any) => { } }
      },
      columnApi: {
        getDisplayedCenterColumns: () => {
          return [{ getColId: () => 1 }];
        }
      },
      api: {
        startEditingCell: (_: any) => { },
        getEditingCells: () => { return [{ rowIndex: 1 }] }
      },
      column: {
        getColId: () => {
          return 'action';
        }
      },
      node: { rowIndex: 2 },
    };
    spyOn(params.context.componentParent, 'edit').and.callFake(() => { });

    component.agInit(params);

    fixture.detectChanges();

    component.edit('edit');

    expect(params.context.componentParent.edit).toHaveBeenCalledTimes(1);
  });
});
