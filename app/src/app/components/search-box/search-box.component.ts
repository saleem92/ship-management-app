import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-search-box',
  templateUrl: './search-box.component.html',
  styleUrls: ['./search-box.component.scss']
})
export class SearchBoxComponent {

  @Output()
  private searchTextEvent: EventEmitter<String> = new EventEmitter();

  /**
   * Emit event when filterText needs to be updated
   * @param event filter text
   */
  searchText(event: any) {
    this.searchTextEvent.emit(event.target.value);
  }

  /**
   * Updates search text when backspace is pressed
   * @param event grid event 
   */
  onKeyDown(event: any) {
    if (event.keyCode === 8) {
      this.searchText(event);
    }
  }
}
