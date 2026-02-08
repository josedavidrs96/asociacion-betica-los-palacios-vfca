import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { ValidateComponent } from './pages/validate/validate.component';
import { AdminComponent } from './pages/admin/admin.component';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'validate', component: ValidateComponent },
  { path: 'admin', component: AdminComponent },
  { path: '**', redirectTo: 'login' }
];
