import { sql } from "@vercel/postgres";
import { json, readJson } from "./jsonResponseUtils.js";
import { requireRole, verifyToken } from "./AuthTokenManagement.js";
import bcrypt from "bcryptjs";
import { randomUUID } from "crypto";

async function checkAdmin(req: any, res: any) {
  const user = await verifyToken(req.headers.authorization);
  try {
    requireRole(user, ["ADMIN"]);
    return user;
  } catch (e: any) {
    json(res, e.message === "FORBIDDEN" ? 403 : 401, { error: "Unauthorized" });
    return null;
  }
}

export async function usersHandler(req: any, res: any) {
  if (!(await checkAdmin(req, res))) return;

  if (req.method === "GET") {
    const r = await sql`SELECT id, username, role, created_at FROM users ORDER BY id DESC`;
    return json(res, 200, { users: r.rows });
  }

  if (req.method === "POST") {
    let body: any;
    try { body = await readJson(req); } catch { return json(res, 400, { error: "Invalid JSON" }); }

    const { username, password, role } = body;
    if (!username || !password || !role) return json(res, 400, { error: "Missing fields" });

    const hash = await bcrypt.hash(password, 10);
    try {
      await sql`INSERT INTO users (username, password_hash, role) VALUES (${username}, ${hash}, ${role})`;
      return json(res, 201, { status: "created" });
    } catch (e: any) {
      return json(res, 500, { error: e.message });
    }
  }

  return json(res, 405, { error: "Method not allowed" });
}

export async function membersHandler(req: any, res: any) {
  if (!(await checkAdmin(req, res))) return;

  if (req.method === "GET") {
    const r = await sql`SELECT * FROM members ORDER BY id DESC`;
    return json(res, 200, { members: r.rows });
  }

  if (req.method === "POST") {
    let body: any;
    try { body = await readJson(req); } catch { return json(res, 400, { error: "Invalid JSON" }); }

    const { full_name, phone, email } = body;
    if (!full_name) return json(res, 400, { error: "Name required" });

    try {
      await sql`INSERT INTO members (full_name, phone, email) VALUES (${full_name}, ${phone}, ${email})`;
      return json(res, 201, { status: "created" });
    } catch (e: any) {
      return json(res, 500, { error: e.message });
    }
  }

  return json(res, 405, { error: "Method not allowed" });
}

export async function passesHandler(req: any, res: any) {
  if (!(await checkAdmin(req, res))) return;

  if (req.method === "GET") {
    const r = await sql`
      SELECT p.*, m.full_name as member_name 
      FROM passes p 
      LEFT JOIN members m ON p.member_id = m.id 
      ORDER BY p.id DESC
    `;
    return json(res, 200, { passes: r.rows });
  }

  if (req.method === "POST") {
    let body: any;
    try { body = await readJson(req); } catch { return json(res, 400, { error: "Invalid JSON" }); }

    const { member_id, total_uses } = body;
    const code = randomUUID().split('-')[0].toUpperCase(); 

    try {
      await sql`
        INSERT INTO passes (member_id, code, total_uses, used_count, active)
        VALUES (${member_id || null}, ${code}, ${total_uses || 5}, 0, true)
      `;
      return json(res, 201, { status: "created", code });
    } catch (e: any) {
      return json(res, 500, { error: e.message });
    }
  }

  return json(res, 405, { error: "Method not allowed" });
}
