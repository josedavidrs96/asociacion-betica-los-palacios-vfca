import { sql } from "@vercel/postgres";
import bcrypt from "bcryptjs";
import { json, readJson } from "../_lib/http.js";
import { signToken } from "../_lib/auth.js";

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
  if (!username || !password) return json(res, 400, { error: "Missing credentials" });

  const r = await sql`SELECT username, password_hash, role FROM users WHERE username = ${username} LIMIT 1`;
  if (r.rowCount === 0) return json(res, 401, { error: "Invalid credentials" });

  const u = r.rows[0] as any;
  const ok = await bcrypt.compare(password, u.password_hash);
  if (!ok) return json(res, 401, { error: "Invalid credentials" });

  const token = await signToken({ username: u.username, role: u.role });
  return json(res, 200, { token, user: { username: u.username, role: u.role } });
}
