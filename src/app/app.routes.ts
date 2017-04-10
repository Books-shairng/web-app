import { Routes } from '@angular/router';
import { LoginComponent } from './login.component';
import { RegistrationComponent } from './registration.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent}
  { path: 'registration', component: RegistrationComponent}
];
