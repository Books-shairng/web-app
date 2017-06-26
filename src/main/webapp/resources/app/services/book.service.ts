import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map'

@Injectable()
export class BookService {
  constructor(private http: Http) {
      var currentUserId = JSON.parse(localStorage.getItem('currentUser.id'));
  }
  notification(id: number) {
    return this.http.get('/api/notification/{'+ id +'}', {})
       .map((response: Response) => {
           let books = response.json();
});
  }
}
