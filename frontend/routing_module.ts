import { Routes } from "@angular/router";
import { LoginComponent } from "./pages/login.component";
import { ValidateComponent } from "./pages/validate.component";

export const routes: Routes = [
  { path: "", pathMatch: "full", redirectTo: "login" },
  { path: "login", component: LoginComponent },
  { path: "validate", component: ValidateComponent },
  { path: "**", redirectTo: "login" }
];
