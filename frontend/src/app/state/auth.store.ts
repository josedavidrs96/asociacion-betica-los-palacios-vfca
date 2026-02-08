import { Injectable, signal, effect } from "@angular/core";

export type Role = "ADMIN" | "ABONADO" | "VALIDATOR";
export type User = { username: string; role: Role };

@Injectable({ providedIn: "root" })
export class AuthStore {
  token = signal<string | null>(localStorage.getItem("token"));
  user = signal<User | null>(JSON.parse(localStorage.getItem("user") || "null"));

  constructor() {
    effect(() => {
      const t = this.token();
      if (t) localStorage.setItem("token", t);
      else localStorage.removeItem("token");
    });
    effect(() => {
      const u = this.user();
      if (u) localStorage.setItem("user", JSON.stringify(u));
      else localStorage.removeItem("user");
    });
  }

  setSession(token: string, user: User) {
    this.token.set(token);
    this.user.set(user);
  }

  logout() {
    this.token.set(null);
    this.user.set(null);
  }
}
