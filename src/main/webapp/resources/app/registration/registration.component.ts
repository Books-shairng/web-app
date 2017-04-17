import {Component} from "@angular/core";
import {NgForm} from "@angular/forms";

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
