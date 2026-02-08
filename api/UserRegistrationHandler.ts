import { sql } from "@vercel/postgres";
import bcrypt from "bcryptjs";
import { json, readJson } from "../_lib/http.js";
import { signToken, Role } from "../_lib/auth.js";

export default async function handler(req: any, res: any) {
  if (req.method !== "POST") return json(res, 405, { error: "Method not allowed" });

  let body: any;
  try {
    body = await readJson(req);
  } catch {
    return json(res, 400, { error: "Invalid JSON" });
  }

  const username = String(body.username ?? "").trim();
  const password = String(body.password ?? "");
  const role = String(body.role ?? "ADMIN") as Role;

  if (!username || password.length < 6) return json(res, 400, { error: "Invalid username/password" });
  if (role !== "ADMIN" && role !== "ABONADO" && role !== "VALIDATOR") return json(res, 400, { error: "Invalid role" });

  const passwordHash = await bcrypt.hash(password, 10);

  try {
    await sql`
      INSERT INTO users (username, password_hash, role)
      VALUES (${username}, ${passwordHash}, ${role})
    `;
  } catch {
    return json(res, 409, { error: "Username already exists" });
  }

  const token = await signToken({ username, role });
  return json(res, 201, { token, user: { username, role } });
}
