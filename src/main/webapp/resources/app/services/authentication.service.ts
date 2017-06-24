import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Subject } from 'rxjs/Subject';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map'

@Injectable()
export class AuthenticationService {
    public token: string;
    private loggedIn: Subject<boolean> = new Subject<boolean>();

    get isLoggedIn() {
      return this.loggedIn.asObservable();
    }
    constructor(private http: Http) {
        // set token if saved in local storage
        var currentUser = JSON.parse(localStorage.getItem('currentUser'));
        this.token = currentUser && currentUser.token;
        this.loggedIn.next(!!localStorage.getItem(this.token));
    }

    login(email: string, password: string): Observable<boolean> {
        return this.http.post('/api/auth', {email:email, password: password})
            .map((response: Response) => {
                // login successful if there's a jwt token in the response
                let token = response.json() && response.json().token;
                if (token) {
                    // set token property
                    this.token = token;

                    // store username and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify({ name: name, email: email, token: token }));
                    // return true to indicate successful login
                    this.loggedIn.next(true);
                    return true;
                } else {
                    // return false to indicate failed login
                    return false;
                }
            });
    }

    logout(): void {
        // clear token remove user from local storage to log user out
        this.token = null;
        localStorage.removeItem('currentUser');
        this.loggedIn.next(false);
    }
}
