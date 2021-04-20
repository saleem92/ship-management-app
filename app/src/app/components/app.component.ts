import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  searchText: string = '';

  /**
   * Text search for global filter
   * @param value search text
   */
  setSearchText(value: any) {
    this.searchText = value;
  }
}
