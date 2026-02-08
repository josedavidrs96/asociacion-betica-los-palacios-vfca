import { Injectable, inject } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { AuthStore } from "../state/auth.store";

@Injectable({ providedIn: "root" })
export class ApiService {
  private http = inject(HttpClient);
  private auth = inject(AuthStore);

  private headers() {
    const t = this.auth.token();
    return t
      ? new HttpHeaders({ Authorization: `Bearer ${t}` })
      : undefined;
  }

  login(username: string, password: string) {
    return this.http.post<any>("/api/auth/login", { username, password });
  }

  register(username: string, password: string, role: string) {
    return this.http.post<any>("/api/auth/register", { username, password, role });
  }

  validate(code: string) {
    return this.http.post<any>("/api/passes/validate", { code }, { headers: this.headers() });
  }

  // Admin methods
  getUsers() {
    return this.http.get<any>("/api/admin/users", { headers: this.headers() });
  }

  createUser(username: string, password: string, role: string) {
    return this.http.post<any>("/api/admin/users", { username, password, role }, { headers: this.headers() });
  }

  getPasses() {
    return this.http.get<any>("/api/admin/passes", { headers: this.headers() });
  }

  createPass(total_uses: number) {
    return this.http.post<any>("/api/admin/passes", { total_uses }, { headers: this.headers() });
  }
}
