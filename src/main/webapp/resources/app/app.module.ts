import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { FormsModule} from '@angular/forms';

import { AppComponent }  from './app.component';
import { routes } from './app.routes';
import { LoginComponent } from './login/index';
import { RegistrationComponent } from './registration/index';
import { SearchComponent } from './search/index';
import { NotificationComponent } from './notifications/index';
import { AuthGuard } from './guards/index';
import { AlertService, AuthenticationService, UserService } from './services/index';
import { AlertComponent } from './alert/index';
// used to create fake backend
import { fakeBackendProvider } from './fakebackend/index';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { BaseRequestOptions } from '@angular/http';


@NgModule({
  imports:      [ BrowserModule, FormsModule, routes, HttpModule ],
  declarations: [ AppComponent, LoginComponent, RegistrationComponent, SearchComponent, NotificationComponent, AlertComponent],
  providers:    [ AuthGuard, AlertService, AuthenticationService, UserService,
                  fakeBackendProvider,
                  MockBackend,
                  BaseRequestOptions],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
