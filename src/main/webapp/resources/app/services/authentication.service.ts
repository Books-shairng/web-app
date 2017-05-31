import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/map';

@Injectable()
export class AuthenticationService {
  // change loggedIn to a subject
  private loggedIn: Subject<boolean> = new Subject<boolean>();

  // make isLoggedIn public readonly
  get isLoggedIn() {
      return this.loggedIn.asObservable();
  }
    constructor(private http: Http) { }

    login(email: string, password: string) {

        var dataObject = {
            email: email,
            password: password,
        }

        return this.http.post('/api/auth', dataObject)
            .map((response: Response) => {
                let user = response.json();
                if (user && user.token) {
                  //JSON.stringify(user) konwentuje model na jsona
                  //setitem przypisuje do localstorga pole currentuser z danynmi sparsowanymi do jsona
                  var logInUser = this.http.get('/api/auth', dataObject)
                    localStorage.setItem('currentUser', JSON.stringify(logInUser));
                }
            });

    }


    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }

}
