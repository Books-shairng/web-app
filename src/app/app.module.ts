import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent }  from './app.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';

import { routes } from './app.routes';

@NgModule({
  imports:      [ BrowserModule ],
  declarations: [ AppComponent, LoginComponent],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
