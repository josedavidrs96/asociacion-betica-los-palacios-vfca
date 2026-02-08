import { Component, inject, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ApiService } from "../../services/api.service";

@Component({
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container">
      <h2>Panel de Administración</h2>
      
      <div class="section">
        <h3>Usuarios (Admin/Validator)</h3>
        <div *ngFor="let u of users" class="item">
          {{ u.username }} ({{ u.role }})
        </div>
        <div class="form">
          <input [(ngModel)]="newUser" placeholder="Usuario" />
          <input [(ngModel)]="newPass" placeholder="Contraseña" type="password" />
          <select [(ngModel)]="newRole">
            <option value="ADMIN">ADMIN</option>
            <option value="VALIDATOR">VALIDATOR</option>
          </select>
          <button (click)="createUser()">Crear Usuario</button>
        </div>
      </div>

      <div class="section">
        <h3>Pases Generados</h3>
        <div *ngFor="let p of passes" class="item">
          Code: <b>{{ p.code }}</b> | Usos: {{ p.used_count }}/{{ p.total_uses }} | Activo: {{ p.active }}
        </div>
        <div class="form">
          <input type="number" [(ngModel)]="newUses" placeholder="Total usos (def: 5)" />
          <button (click)="createPass()">Generar Pase Anónimo</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .container { padding: 20px; }
    .section { border: 1px solid #ccc; padding: 15px; margin-bottom: 20px; border-radius: 8px; }
    .item { padding: 5px 0; border-bottom: 1px solid #eee; }
    .form { margin-top: 10px; display: flex; gap: 10px; }
    input, select { padding: 5px; }
  `]
})
export class AdminComponent implements OnInit {
  private api = inject(ApiService);

  users: any[] = [];
  passes: any[] = [];

  newUser = "";
  newPass = "";
  newRole = "VALIDATOR";

  newUses = 5;

  ngOnInit() {
    this.loadData();
  }

  loadData() {
    this.api.getUsers().subscribe(d => this.users = d.users);
    this.api.getPasses().subscribe(d => this.passes = d.passes);
  }

  createUser() {
    if (!this.newUser || !this.newPass) return;
    this.api.createUser(this.newUser, this.newPass, this.newRole).subscribe(() => {
      this.newUser = "";
      this.newPass = "";
      this.loadData();
    });
  }

  createPass() {
    this.api.createPass(this.newUses).subscribe(() => {
      this.loadData();
    });
  }
}
