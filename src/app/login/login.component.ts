import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Http } from '@angular/http';
import { FormsModule } from '@angular/forms';
import { NgForm } from '@angular/forms';

import { Router, ActivatedRoute } from '@angular/router';
import { HttpService } from '../services/index';

@Component({
  moduleId: module.id,
  templateUrl: `login.component.html`,
})
export class LoginComponent  {

constructor(
  private route: ActivatedRoute,
  private router: Router,
  private httpService: HttpService,
  ) {}

  loginFunction() {
  this.httpService.loginFunction(this.login, this.password)
            .subscribe(
                data => {
                    [routerLink]="./search/search.component";
                });
    }
}

  }
}
