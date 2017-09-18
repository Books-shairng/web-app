import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs';
import { Book } from '../models/book';
import 'rxjs/add/operator/map'

@Injectable()
export class BookService {
  constructor(private http: Http) { }

  addBook(book: Book) {
    let obj = JSON.parse(localStorage.getItem("currentUser"));
    let headers = new Headers({ 'Authorization': 'Bearer ' + obj.token });
    let options = new RequestOptions({ headers: headers });
    return this.http.post('/api/books', book, options).map((response: Response) => response.json());
  }

}
