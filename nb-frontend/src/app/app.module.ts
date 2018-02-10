import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpModule} from '@angular/http';
import {FormsModule} from '@angular/forms';
import {HashLocationStrategy, LocationStrategy} from '@angular/common';

import {AppComponent} from './app.component';
import {routes} from './app.routes';
import {LoginComponent} from './login/index';
import {RegistrationComponent} from './registration/index';
import {SearchComponent} from './search/index';
import {NotificationComponent} from './notifications/index';
import {AuthGuard} from './guards/index';
import {AuthenticationService, BookService, UserService} from './services/index';
import {AddBookComponent} from './addbook/index';
import {BookInfoComponent} from './bookinfo/index';
import {BooksListComponent} from './bookslist/index';
import {SettingsComponent} from './settings/index';

@NgModule({
  imports: [BrowserModule, FormsModule, routes, HttpModule],
  declarations: [AppComponent, LoginComponent, RegistrationComponent, SearchComponent, NotificationComponent,
    AddBookComponent, BookInfoComponent, BooksListComponent, SettingsComponent],
  providers: [AuthGuard, AuthenticationService, UserService, BookService,
    {provide: LocationStrategy, useClass: HashLocationStrategy}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
