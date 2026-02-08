import { sql } from "@vercel/postgres";
import { json, readJson } from "./jsonResponseUtils.js";
import { signToken } from "./AuthTokenManagement.js";
import bcrypt from "bcryptjs";

export async function loginHandler(req: any, res: any) {
  if (req.method !== "POST") return json(res, 405, { error: "Method not allowed" });

  let body: any;
  try {
    body = await readJson(req);
  } catch {
    return json(res, 400, { error: "Invalid JSON" });
  }

  const { username, password } = body;
  if (!username || !password) return json(res, 400, { error: "Missing credentials" });

  const r = await sql`SELECT * FROM users WHERE username = ${username} LIMIT 1`;
  if (r.rowCount === 0) return json(res, 401, { error: "Invalid credentials" });

  const user = r.rows[0];
  const valid = await bcrypt.compare(password, user.password_hash);
  if (!valid) return json(res, 401, { error: "Invalid credentials" });

  const token = await signToken({ username: user.username, role: user.role });
  return json(res, 200, { token, user: { username: user.username, role: user.role } });
}

export async function registerHandler(req: any, res: any) {
  if (req.method !== "POST") return json(res, 405, { error: "Method not allowed" });

  let body: any;
  try {
    body = await readJson(req);
  } catch {
    return json(res, 400, { error: "Invalid JSON" });
  }

  const { username, password, role } = body;
  if (!username || !password || !role) return json(res, 400, { error: "Missing fields" });
  if (password.length < 6) return json(res, 400, { error: "Password too short" });

  const hash = await bcrypt.hash(password, 10);

  try {
    await sql`
      INSERT INTO users (username, password_hash, role)
      VALUES (${username}, ${hash}, ${role})
    `;
    return json(res, 201, { user: { username, role } });
  } catch (e: any) {
    if (e.code === "23505") return json(res, 409, { error: "Username taken" });
    return json(res, 500, { error: "Database error" });
  }
}
