import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { AlertService, UserService } from '../services/index';

@Component({
  selector: 'registration-app',
  moduleId: module.id,
  templateUrl: `registration.component.html`,
})
export class RegistrationComponent  {
model: any = {};
 loading = false;

 constructor(
     private router: Router,
     private userService: UserService,
     private alertService: AlertService) { }

 register() {
     this.loading = true;
     this.userService.create(this.model)
         .subscribe(
             data => {
               this.alertService.success('Registration successful', true);
               this.router.navigate(['/login']);
             },
             error => {
                  this.alertService.error(error);
                 this.loading = false;
             });
 }
}
