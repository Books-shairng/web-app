import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'my-app',
  moduleId: module.id,
  templateUrl: `app.component.html`,
})
export class AppComponent  {
  constructor(public router: Router) {}
 }
