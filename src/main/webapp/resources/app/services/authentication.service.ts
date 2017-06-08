import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import 'rxjs/add/operator/map';

@Injectable()
export class AuthenticationService {
    public token: string;
    constructor(private http: Http) {
        var currentUser = JSON.parse(localStorage.getItem('currentUser'))
        this.token = currentUser && currentUser.token
    }

    login(email: string, password: string): Observable<boolean> {
        return this.http.post('/api/auth', { email: email, password: password })
            .map((response: Response) => {
                let token = response.json() && response.json().token;
                if (token) {
                    this.token = token
                }
                localStorage.setItem('currentUser', JSON.stringify({ email: email, token: token }));
                return true;
            }

            )
    };




    logout() {
        // remove user from local storage to log user out
        this.token = null;
        localStorage.removeItem('currentUser');
    }

}
