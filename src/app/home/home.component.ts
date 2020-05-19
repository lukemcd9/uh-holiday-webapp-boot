import { Component, OnInit } from '@angular/core';
import {HolidayService} from "../holiday.service";
import {Holiday} from "../holiday";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  holidays: Holiday[];
  year: number = new Date().getFullYear();

  constructor(private holidayService: HolidayService) {
  }

  ngOnInit(): void {
    this.getHolidays();
  }

  getHolidays(): void {
    this.holidayService.findAll().subscribe(data => {
      this.holidays = data;
    });
  }

  getHolidaysByYear(): Holiday[] {
    if (this.holidays) {
      return this.holidays.filter(holiday => holiday.year === this.year);
    } else {
      return [];
    }
  }

  getAllYears(): Array<number> {
    if (this.holidays) {
      let years = this.holidays.map(holiday => holiday.year);
      return years.filter((year, index) => years.indexOf(year) === index);
    } else {
      return [];
    }
  }
}
