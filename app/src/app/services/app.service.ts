import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ship } from '../models/Ship';

import { catchError } from 'rxjs/operators';

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AppService {

  private baseUrl = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) { }

  public getShips(): Observable<Ship[]> {
    return this.httpClient.get<Ship[]>(this.baseUrl, this.getHttpHeaders());
  }

  public createShip(ship: Ship): Observable<Ship[]> {
    return this.httpClient.post<Ship[]>(this.baseUrl, ship, this.getHttpHeaders());
  }

  public updateShipDetails(ship: Ship): Observable<Ship> {
    return this.httpClient.patch(`${this.baseUrl}/${ship.id}`, ship, this.getHttpHeaders());
  }

  public deleteShip(id: number): Observable<any> {
    return this.httpClient.delete(`${this.baseUrl}/${id}`, this.getHttpHeaders());
  }

  private getHttpHeaders(): { headers: HttpHeaders } {
    return {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }
  }
}
