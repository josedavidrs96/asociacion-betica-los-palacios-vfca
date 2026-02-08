import { SignJWT, jwtVerify } from "jose";
import { mustGetEnv } from "./env.js";

const encoder = new TextEncoder();

export type Role = "ADMIN" | "ABONADO" | "VALIDATOR";

export type AuthUser = {
  username: string;
  role: Role;
};

export async function signToken(user: AuthUser): Promise<string> {
  const secret = mustGetEnv("AUTH_JWT_SECRET");
  return await new SignJWT({ role: user.role })
    .setProtectedHeader({ alg: "HS256" })
    .setSubject(user.username)
    .setIssuedAt()
    .setExpirationTime("7d")
    .sign(encoder.encode(secret));
}

export async function verifyToken(authHeader?: string): Promise<AuthUser | null> {
  if (!authHeader) return null;
  const m = authHeader.match(/^Bearer\s+(.+)$/i);
  if (!m) return null;

  const secret = mustGetEnv("AUTH_JWT_SECRET");
  const token = m[1];

  try {
    const { payload } = await jwtVerify(token, encoder.encode(secret));
    const username = payload.sub;
    const role = payload["role"];
    if (typeof username !== "string") return null;
    if (role !== "ADMIN" && role !== "ABONADO" && role !== "VALIDATOR") return null;
    return { username, role };
  } catch {
    return null;
  }
}

export function requireRole(user: AuthUser | null, roles: Role[]): void {
  if (!user) throw new Error("UNAUTHORIZED");
  if (!roles.includes(user.role)) throw new Error("FORBIDDEN");
}
