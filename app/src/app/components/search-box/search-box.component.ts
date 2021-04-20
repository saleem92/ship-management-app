import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-search-box',
  templateUrl: './search-box.component.html',
  styleUrls: ['./search-box.component.scss']
})
export class SearchBoxComponent {

  @Output()
  private searchTextEvent: EventEmitter<String> = new EventEmitter();

  searchText(event: any) {
    console.log(event)
    this.searchTextEvent.emit(event.target.value);
  }

  onKeyDown(event: any) {
    if (event.keyCode === 8) {
      this.searchText(event);
    }
  }
}
