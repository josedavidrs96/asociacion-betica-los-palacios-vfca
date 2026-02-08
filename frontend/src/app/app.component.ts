import { Component, inject } from '@angular/core';
import { RouterOutlet, Router } from '@angular/router';
import { AuthStore } from './state/auth.store';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule],
  template: `
    <header class="header">
      <div class="header-content">
        <img src="/assets/betis-logo.png" alt="Logo Betis" class="logo">
        <h1>Peña Bética Los Palacios</h1>
        <div *ngIf="auth.user() as u" class="user-info">
          <span>Hola, {{ u.username }} ({{ u.role }})</span>
          <button (click)="logout()">Salir</button>
        </div>
      </div>
    </header>
    <main class="main-content">
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .header { 
      background-color: var(--surface-dark);
      padding: 15px 30px;
      border-bottom: 2px solid var(--betis-green);
      box-shadow: 0 2px 8px rgba(0,0,0,0.3);
    }
    .header-content {
      display: flex; 
      justify-content: space-between; 
      align-items: center;
      max-width: 1200px;
      margin: 0 auto;
    }
    .logo {
      height: 50px;
      margin-right: 20px;
    }
    h1 {
      margin: 0;
      font-size: 1.8rem;
      flex-grow: 1;
    }
    .user-info {
      display: flex;
      align-items: center;
      gap: 15px;
    }
    .user-info button {
      width: auto;
      padding: 8px 16px;
      background-color: transparent;
      border: 1px solid var(--betis-green);
      color: var(--betis-green);
    }
    .user-info button:hover {
      background-color: var(--betis-green);
      color: white;
    }
    .main-content {
      padding: 20px;
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
