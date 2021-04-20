import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchBoxComponent } from './search-box.component';

describe('SearchBoxComponent', () => {
  let component: SearchBoxComponent;
  let fixture: ComponentFixture<SearchBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchBoxComponent]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit value when keypress', () => {
    spyOn(component['searchTextEvent'], 'emit');

    const div: HTMLElement = fixture.nativeElement.querySelector('input');
    div.dispatchEvent(new Event('keyup', {}));

    fixture.detectChanges();

    expect(component['searchTextEvent']['emit']).toHaveBeenCalledTimes(1);
  });
});
