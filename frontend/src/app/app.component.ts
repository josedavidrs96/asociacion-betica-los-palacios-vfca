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
      <div class="title-container">
        <img src="https://assets.stickpng.com/images/584ad18b5503e5a2c307e020.png" alt="Logo Real Betis" class="logo">
        <h1>Asociación Bética Los Palacios</h1>
      </div>
      <div *ngIf="auth.user() as u" class="user-info">
        <span>Hola, {{ u.username }} ({{ u.role }})</span>
        <button (click)="logout()">Salir</button>
      </div>
    </div>
    <router-outlet></router-outlet>
  `,
  styles: [`
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid var(--border-color);
      padding: 10px 20px;
      margin-bottom: 20px;
    }
    .title-container {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    .logo {
      height: 50px;
    }
    .user-info {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    button {
      width: auto;
      margin: 0;
      padding: 8px 15px;
    }
    h1 {
      font-size: 1.5rem;
    }
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
