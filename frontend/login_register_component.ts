import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { ApiService } from "../services/api.service";
import { AuthStore } from "../state/auth.store";

@Component({
  standalone: true,
  imports: [FormsModule],
  template: `
    <div class="card">
      <h2>Login</h2>
      <input [(ngModel)]="username" placeholder="Usuario" />
      <input [(ngModel)]="password" placeholder="Contraseña" type="password" />
      <button (click)="doLogin()">Entrar</button>
      <p class="bad" *ngIf="error">{{ error }}</p>
      <p class="ok" *ngIf="ok">{{ ok }}</p>
    </div>

    <div class="card">
      <h2>Registro (demo)</h2>
      <input [(ngModel)]="regUser" placeholder="Usuario" />
      <input [(ngModel)]="regPass" placeholder="Contraseña (mín. 6)" type="password" />
      <select [(ngModel)]="regRole">
        <option value="ADMIN">ADMIN</option>
        <option value="VALIDATOR">VALIDATOR</option>
        <option value="ABONADO">ABONADO</option>
      </select>
      <button (click)="doRegister()">Crear usuario</button>
      <p class="bad" *ngIf="regError">{{ regError }}</p>
      <p class="ok" *ngIf="regOk">{{ regOk }}</p>
    </div>
  `
})
export class LoginComponent {
  private api = inject(ApiService);
  private auth = inject(AuthStore);

  username = "";
  password = "";
  error = "";
  ok = "";

  regUser = "";
  regPass = "";
  regRole = "VALIDATOR";
  regError = "";
  regOk = "";

  doLogin() {
    this.error = "";
    this.ok = "";
    this.api.login(this.username.trim(), this.password).subscribe({
      next: (data) => {
        this.auth.setSession(data.token, data.user);
        this.ok = `Sesión iniciada como ${data.user.username} (${data.user.role})`;
      },
      error: (e) => {
        this.error = e?.error?.error ?? "Login inválido";
      }
    });
  }

  doRegister() {
    this.regError = "";
    this.regOk = "";
    this.api.register(this.regUser.trim(), this.regPass, this.regRole).subscribe({
      next: (data) => {
        this.regOk = `Usuario creado: ${data.user.username} (${data.user.role})`;
      },
      error: (e) => {
        this.regError = e?.error?.error ?? "Registro inválido";
      }
    });
  }
}
