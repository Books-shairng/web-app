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

        let dataObject = {
            email: email,
            password: password,
        }

        return this.http.post('/api/auth', dataObject)
            .map((response: Response) => {
                let user = response.json();
                console.log(user);
                console.log(dataObject);
                if (user && user.token) {
                  let loginUser = {
                    id: user.id,
                    email: user.email,
                    name: user.firstName + " " + user.lastName,
                  //  token: 'fake-jwt-token'
                  }
                  console.log(loginUser);
                  localStorage.setItem('currentUser', JSON.stringify(loginUser));
                }
            });

    }


    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }

}
