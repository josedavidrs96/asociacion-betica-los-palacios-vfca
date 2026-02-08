import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { CommonModule } from "@angular/common";
import { ApiService } from "../../services/api.service";

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
    <div class="card">
      <h2>Validación (ADMIN / VALIDATOR)</h2>
      <input [(ngModel)]="code" placeholder="Código" />
      <button (click)="doValidate()">Validar</button>

      <p class="ok" *ngIf="status==='ok'">OK. Restantes: {{ remaining }}</p>
      <p class="bad" *ngIf="status && status!=='ok'">Estado: {{ status }}</p>
      <p class="bad" *ngIf="error">{{ error }}</p>
    </div>
  `
})
export class ValidateComponent {
  private api = inject(ApiService);

  code = "";
  status: string = "";
  remaining: number | null = null;
  error = "";

  doValidate() {
    this.status = "";
    this.remaining = null;
    this.error = "";

    this.api.validate(this.code.trim()).subscribe({
      next: (data) => {
        this.status = data.status;
        this.remaining = data.remaining ?? null;
      },
      error: (e) => {
        this.status = e?.error?.status ?? "";
        this.error = e?.error?.error ?? "";
      }
    });
  }
}
