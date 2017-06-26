import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from '@angular/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppComponent }  from './app.component';
import { routes } from './app.routes';
import { LoginComponent } from './login/index';
import { RegistrationComponent } from './registration/index';
import { SearchComponent } from './search/index';
import { NotificationComponent } from './notifications/index';
import { AuthGuard } from './guards/index';
import { AuthenticationService, UserService, BookService } from './services/index';
import { AddBookComponent } from './addbook/index';
import { BookInfoComponent } from './bookinfo/index';
import { BooksListComponent } from './bookslist/index';
import { SettingsComponent } from './settings/index';
//used to create fake backend
import { fakeBackendProvider } from './fakebackend/index';
import { MockBackend, MockConnection } from '@angular/http/testing';
import { BaseRequestOptions } from '@angular/http';;


@NgModule({
    imports: [BrowserModule, FormsModule, routes, HttpModule],
    declarations: [AppComponent, LoginComponent, RegistrationComponent, SearchComponent, NotificationComponent, AddBookComponent, BookInfoComponent, BooksListComponent, SettingsComponent],
    providers: [AuthGuard, AuthenticationService, UserService, BookService],
    // fakeBackendProvider,
    // MockBackend,
    // BaseRequestOptions],
    bootstrap: [AppComponent]
})
export class AppModule { }
