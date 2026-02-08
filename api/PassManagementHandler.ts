import { sql } from "@vercel/postgres";
import { json, readJson } from "../_lib/http.js";
import { requireRole, verifyToken } from "../_lib/auth.js";

function randomCode(len = 10) {
  const chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
  let out = "";
  for (let i = 0; i < len; i++) out += chars[Math.floor(Math.random() * chars.length)];
  return out;
}

export default async function handler(req: any, res: any) {
  const user = await verifyToken(req.headers.authorization);

  if (req.method === "GET") {
    try {
      requireRole(user, ["ADMIN"]);
    } catch (e: any) {
      return json(res, e.message === "FORBIDDEN" ? 403 : 401, { error: "Unauthorized" });
    }

    const r = await sql`
      SELECT p.*, m.full_name
      FROM passes p
      LEFT JOIN members m ON m.id = p.member_id
      ORDER BY p.id DESC
      LIMIT 200
    `;
    return json(res, 200, { passes: r.rows });
  }

  if (req.method === "POST") {
    try {
      requireRole(user, ["ADMIN"]);
    } catch (e: any) {
      return json(res, e.message === "FORBIDDEN" ? 403 : 401, { error: "Unauthorized" });
    }

    let body: any;
    try {
      body = await readJson(req);
    } catch {
      return json(res, 400, { error: "Invalid JSON" });
    }

    const memberId = Number(body.member_id);
    const totalUses = body.total_uses ? Number(body.total_uses) : 5;

    if (!Number.isFinite(memberId)) return json(res, 400, { error: "member_id required" });
    if (!Number.isFinite(totalUses) || totalUses <= 0) return json(res, 400, { error: "total_uses invalid" });

    let code = randomCode(10);
    for (let i = 0; i < 5; i++) {
      const exists = await sql`SELECT 1 FROM passes WHERE code = ${code} LIMIT 1`;
      if (exists.rowCount === 0) break;
      code = randomCode(10);
    }

    const created = await sql`
      INSERT INTO passes (member_id, code, total_uses, used_count, active)
      VALUES (${memberId}, ${code}, ${totalUses}, 0, true)
      RETURNING *
    `;

    return json(res, 201, { pass: created.rows[0] });
  }

  return json(res, 405, { error: "Method not allowed" });
}
