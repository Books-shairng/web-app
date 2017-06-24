import { Component } from '@angular/core';
import { AuthenticationService } from './services/authentication.service';
import { Router } from '@angular/router';

@Component({
    selector: 'my-app',
    moduleId: module.id,
    templateUrl: `app.component.html`,
})
export class AppComponent {
  loggedIn: any;
  constructor(private authenticationService: AuthenticationService,
              private router: Router){
                this.loggedIn = this.authenticationService.isLoggedIn;
  }

  }
