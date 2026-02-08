import { registerHandler } from "../jsonResponseHandlers.js";

export default async function handler(req: any, res: any) {
  return registerHandler(req, res);
}
