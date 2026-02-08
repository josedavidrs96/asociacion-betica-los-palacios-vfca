import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { Router } from "@angular/router";
import { ApiService } from "../../services/api.service";
import { AuthStore } from "../../state/auth.store";

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <div class="card">
      <h2>Login</h2>
      <input [(ngModel)]="username" placeholder="Usuario" />
      <input [(ngModel)]="password" placeholder="Contraseña" type="password" />
      <button (click)="doLogin()">Entrar</button>
      <p class="bad" *ngIf="error">{{ error }}</p>
      <p class="ok" *ngIf="ok">{{ ok }}</p>
    </div>
  `
})
export class LoginComponent {
  private api = inject(ApiService);
  private auth = inject(AuthStore);
  private router = inject(Router);

  username = "";
  password = "";
  error = "";
  ok = "";

  doLogin() {
    this.error = "";
    this.ok = "";
    this.api.login(this.username.trim(), this.password).subscribe({
      next: (data) => {
        this.auth.setSession(data.token, data.user);
        this.ok = `Sesión iniciada como ${data.user.username} (${data.user.role})`;
        
        if (data.user.role === 'ADMIN') {
          this.router.navigate(['/admin']);
        } else {
          this.router.navigate(['/validate']);
        }
      },
      error: (e) => {
        this.error = e?.error?.error ?? "Login inválido";
      }
    });
  }
}
