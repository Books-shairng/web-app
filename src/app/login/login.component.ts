import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'login-app',
  moduleId: module.id,
  templateUrl: `login.html`,
})
export class LoginComponent  {
  loginComponent(event, email-adress, password){


  registration(event) {
  event.preventDefault();
  this.router.navigate(['registration']);
}
  }
}
