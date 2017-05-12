import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { FormsModule} from '@angular/forms';

import { AppComponent }  from './app.component';
import { routes } from './app.routes';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { SearchComponent } from './search/search.component';
import { NotificationComponent } from './notifications/index';
import { AuthGuard } from './guards/index';
import { AuthenticationService, UserService } from './services/index';
// used to create fake backend
// import { fakeBackendProvider } from './fakebackend/index';
// import { MockBackend, MockConnection } from '@angular/http/testing';
// import { BaseRequestOptions } from '@angular/http';


@NgModule({
  imports:      [ BrowserModule, FormsModule, routes, HttpModule ],
  declarations: [ AppComponent, LoginComponent, RegistrationComponent, SearchComponent, NotificationComponent],
  providers:    [ AuthGuard, AuthenticationService, UserService,],
                  // fakeBackendProvider,
                  // MockBackend,
                  // BaseRequestOptions],
  bootstrap:    [ AppComponent ]
})
export class AppModule { }
