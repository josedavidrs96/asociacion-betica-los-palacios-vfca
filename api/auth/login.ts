import { loginHandler } from "../jsonResponseHandlers.js";

export default async function handler(req: any, res: any) {
  return loginHandler(req, res);
}
