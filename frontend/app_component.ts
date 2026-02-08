import { Component, computed, inject } from "@angular/core";
import { RouterLink, RouterOutlet } from "@angular/router";
import { AuthStore } from "./state/auth.store";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [RouterLink, RouterOutlet],
  template: `
    <div class="container">
      <h1>Asociación Bética Los Palacios VFCA</h1>

      <div class="card">
        <div class="row">
          <a routerLink="/login">Login</a>
          <a routerLink="/validate">Validar</a>
        </div>
        <p>
          Estado:
          <strong [class.ok]="isLoggedIn()" [class.bad]="!isLoggedIn()">
            {{ isLoggedIn() ? ('OK (' + username() + ' / ' + role() + ')') : 'Sin sesión' }}
          </strong>
        </p>
        <button (click)="logout()" [disabled]="!isLoggedIn()">Cerrar sesión</button>
      </div>

      <router-outlet />
    </div>
  `
})
export class AppComponent {
  private auth = inject(AuthStore);

  isLoggedIn = computed(() => this.auth.token() !== null);
  username = computed(() => this.auth.user()?.username ?? "-");
  role = computed(() => this.auth.user()?.role ?? "-");

  logout() {
    this.auth.logout();
  }
}
