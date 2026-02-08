import { Component, inject } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { AuthStore } from './state/auth.store';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <div class="header">
      <h1>Asociación Bética Los Palacios</h1>
      <div *ngIf="auth.user() as u">
        <span>Hola, {{ u.username }} ({{ u.role }})</span>
        <button (click)="logout()">Salir</button>
      </div>
    </div>
    <router-outlet></router-outlet>
  `,
  styles: [`
    .header { display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #ccc; padding-bottom: 10px; margin-bottom: 20px; }
    button { width: auto; margin: 0; padding: 5px 10px; }
  `]
})
export class AppComponent {
  auth = inject(AuthStore);
  private router = inject(Router);

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
