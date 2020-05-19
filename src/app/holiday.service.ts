import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Holiday} from "./holiday";

@Injectable({
  providedIn: 'root'
})
export class HolidayService {

  private holidaysUrl: string;

  constructor(private http: HttpClient) {
    this.holidaysUrl = "http://localhost:8080/holiday/api/holidays";
  }

  public findAll(): Observable<Holiday[]> {
    return this.http.get<Holiday[]>(this.holidaysUrl);
  }
}
