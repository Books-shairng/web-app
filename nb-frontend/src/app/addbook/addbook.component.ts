import { Component, OnInit } from '@angular/core';
import { BookService } from '../services/index';

@Component({
  templateUrl: './addbook.component.html'
})

export class AddBookComponent {
  ///api/books/{bookID}
  model: any = {};
  constructor(
    private bookService: BookService) { }

  createBook() {
    this.bookService.addBook(this.model).subscribe(data => {
      console.log(data)
    });
  }
}
