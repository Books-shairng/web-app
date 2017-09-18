import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs';
import 'rxjs/add/operator/map'

import { AuthenticationService } from '../services/index';
import { User } from '../models/index';

@Injectable()
export class UserService {
  constructor(
    private http: Http,
    private authenticationService: AuthenticationService) {
  }
  create(user: User) {
    return this.http.post('/api/users', user).map((response: Response) => response.json());

  }
}
