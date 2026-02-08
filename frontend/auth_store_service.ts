import { Injectable, signal } from "@angular/core";

export type Role = "ADMIN" | "ABONADO" | "VALIDATOR";
export type User = { username: string; role: Role };

@Injectable({ providedIn: "root" })
export class AuthStore {
  token = signal<string | null>(null);
  user = signal<User | null>(null);

  setSession(token: string, user: User) {
    this.token.set(token);
    this.user.set(user);
  }

  logout() {
    this.token.set(null);
    this.user.set(null);
  }
}
