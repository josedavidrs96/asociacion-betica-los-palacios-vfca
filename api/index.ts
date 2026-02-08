import { loginHandler, registerHandler } from './_lib/jsonResponseHandlers.js';
import validatePassHandler from './_lib/GetPassDetailsHandler.js';
import { usersHandler, membersHandler, passesHandler } from './_lib/AdminHandlers.js';
import { json } from './_lib/jsonResponseUtils.js';

export default async function handler(req: any, res: any) {
  const url = new URL(req.url, `http://${req.headers.host}`);
  const path = url.pathname;

  // CORS preflight
  if (req.method === 'OPTIONS') {
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    return res.status(204).send('');
  }
  
  // Add CORS headers to actual responses
  res.setHeader('Access-Control-Allow-Origin', '*');

  try {
    if (path.startsWith('/api/auth/login')) {
      return await loginHandler(req, res);
    }
    if (path.startsWith('/api/auth/register')) {
      return await registerHandler(req, res);
    }
    if (path.startsWith('/api/passes/validate')) {
      return await validatePassHandler(req, res);
    }
    if (path.startsWith('/api/admin/users')) {
      return await usersHandler(req, res);
    }
    if (path.startsWith('/api/admin/members')) {
      return await membersHandler(req, res);
    }
    if (path.startsWith('/api/admin/passes')) {
      return await passesHandler(req, res);
    }

    return json(res, 404, { error: 'Not Found' });
  } catch (e: any) {
    console.error("Unhandled API error:", e);
    return json(res, 500, { error: "Internal Server Error", details: e.message });
  }
}
