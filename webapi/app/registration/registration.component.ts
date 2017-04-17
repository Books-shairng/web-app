import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'registration-app',
  moduleId: module.id,
  templateUrl: `registration.component.html`,
})
export class RegistrationComponent  {
  onRegSubmit(r: NgForm){
    console.log(r.value);
  }
}
