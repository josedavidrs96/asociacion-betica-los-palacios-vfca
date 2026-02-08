import sql from "./db.js";
import { json, readJson } from "./jsonResponseUtils.js";
import { requireRole, verifyToken } from "./AuthTokenManagement.js";

export default async function handler(req: any, res: any) {
  const user = await verifyToken(req.headers.authorization);

  if (req.method !== "POST") return json(res, 405, { error: "Method not allowed" });

  try {
    requireRole(user, ["ADMIN", "VALIDATOR"]);
  } catch (e: any) {
    return json(res, e.message === "FORBIDDEN" ? 403 : 401, { error: "Unauthorized" });
  }

  let body: any;
  try {
    body = await readJson(req);
  } catch {
    return json(res, 400, { error: "Invalid JSON" });
  }

  const code = String(body.code ?? "").trim();
  if (!code) return json(res, 400, { error: "code required" });

  try {
    const r = await sql`
      SELECT id, total_uses, used_count, active
      FROM passes
      WHERE code = ${code}
      LIMIT 1
    `;

    if (r.length === 0) {
      await sql`INSERT INTO ride_uses (pass_id, validator_username, result) VALUES (NULL, ${user!.username}, 'not_found')`;
      return json(res, 404, { status: "not_found" });
    }

    const p = r[0] as any;

    if (!p.active) {
      await sql`INSERT INTO ride_uses (pass_id, validator_username, result) VALUES (${p.id}, ${user!.username}, 'inactive')`;
      return json(res, 409, { status: "inactive" });
    }

    if (p.used_count >= p.total_uses) {
      await sql`INSERT INTO ride_uses (pass_id, validator_username, result) VALUES (${p.id}, ${user!.username}, 'no_remaining')`;
      return json(res, 409, { status: "no_remaining", remaining: 0 });
    }

    await sql`UPDATE passes SET used_count = used_count + 1 WHERE id = ${p.id}`;
    await sql`INSERT INTO ride_uses (pass_id, validator_username, result) VALUES (${p.id}, ${user!.username}, 'ok')`;

    const remaining = Number(p.total_uses) - (Number(p.used_count) + 1);
    return json(res, 200, { status: "ok", remaining });
  } catch (e: any) {
    console.error("Database error in GetPassDetailsHandler:", e);
    return json(res, 500, { error: "Database error", details: e.message });
  }
}
