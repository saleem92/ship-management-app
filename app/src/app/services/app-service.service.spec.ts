import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { AppService } from './app.service';
import { Ship } from '../models/Ship';

import { environment } from "../../environments/environment";

describe('App Service', () => {
  let httpMock: HttpTestingController;
  let service: AppService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AppService]
    });
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(AppService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('Should return an Observable of ships list', () => {
    const response: Ship[] = [{
      id: 1,
      name: 'Ship 1',
      code: 'AAAA-AAAA-A0',
      length: 0.1,
      width: 0.2
    }];

    service.getShips().subscribe(res => {
      expect(res).toEqual(response);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}`);
    expect(req.request.method).toBe('GET');
    req.flush(response);
  });

  it('Should return an Observable of patched ship', () => {
    const data: Ship = {
      id: 1,
      name: 'Ship 1',
      code: 'AAAA-AAAA-A0',
      length: 0.1,
      width: 0.2
    };

    service.updateShipDetails(data).subscribe(res => {
      expect(res).toEqual(data);
    });

    const req = httpMock.expectOne(`${environment.apiBaseUrl}/${data.id}`);
    expect(req.request.method).toBe('PATCH');
    req.flush(data);
  });
});
