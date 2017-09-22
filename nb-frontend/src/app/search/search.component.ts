import { Component } from '@angular/core';
import { BookService } from '../services/index';

@Component({
    templateUrl: `./search.component.html`,
})
export class SearchComponent {

  constructor(
    private bookService: BookService) { }

}
