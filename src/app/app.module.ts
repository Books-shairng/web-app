import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { routes } from './app.routes';
import { HttpModule } from '@angular/http';
import { FormsModule} from '@angular/forms';


import { AppComponent }  from './app.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';

@NgModule({
  imports:      [ BrowserModule, routes, HttpModule ],
  declarations: [ AppComponent, LoginComponent, RegistrationComponent],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
