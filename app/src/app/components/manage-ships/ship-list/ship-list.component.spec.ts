import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { RouterTestingModule } from '@angular/router/testing';
import { AgGridModule } from 'ag-grid-angular';
import { of } from 'rxjs';
import { Ship } from 'src/app/models/Ship';
import { AppService } from 'src/app/services/app.service';

import { ShipListComponent } from './ship-list.component';

describe('ShipListComponent', () => {
  let component: ShipListComponent;
  let fixture: ComponentFixture<ShipListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShipListComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatDialogModule,
        MatIconModule,
        AgGridModule.forRoot()
      ],
      providers: [AppService]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ShipListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    spyOn(component['appService'], 'getShips')
      .and.callFake(() => of([new Ship(10, 'item 1', 'xxx-xxx-x5', 5.0, 10.0)]));
  });

  it('should open ship creation dialog component', () => {
    spyOn(<any>component['dialog'], 'open').and.returnValue({
      afterClosed() {
        return of(true);
      },
    });

    component.openDialog();

    expect(component['dialog'].open).toHaveBeenCalled();
  });

  it('should update rowData after ship creation', () => {
    spyOn(<any>component['dialog'], 'open').and.returnValue({
      afterClosed() {
        const ship = new Ship(undefined, 'test', 'xxx-xxx-x0', 0.1, 0.2);
        return of(ship);
      },
    });

    spyOn(<any>component['appService'], 'createShip')
      .and.callFake(() => of(new Ship(1, 'test', 'xxx-xxx-x0', 0.1, 0.2)));

    const icon = fixture.debugElement.nativeElement.querySelector('#create-ship-icon');
    icon.click();

    expect(<any>component['dialog'].open).toHaveBeenCalled();
    expect(<any>component['appService'].createShip).toHaveBeenCalled();

    expect(component.rowData).toHaveSize(1);
  });
});
