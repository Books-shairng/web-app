import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from '../services/index';

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
     private userService: UserService) { }

 register() {
     this.loading = true;
     this.userService.create(this.model)
         .subscribe(
             data => {
                 this.router.navigate(['/login']);
             },
             error => {
                 this.loading = false;
             });
 }
}
