import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { SearchComponent } from './search/search.component';

export const router: Routes = [
{ path: '',       component: LoginComponent },
{ path: 'login',  component: LoginComponent },
{ path: 'registration',  component: RegistrationComponent },
{ path: 'search',  component: SearchComponent },
{ path: '**',     redirectTo: '' },
];

export const routes: ModuleWithProviders = RouterModule.forRoot(router);
