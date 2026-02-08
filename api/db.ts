import { neon } from '@neondatabase/serverless';
import { mustGetEnv } from './getEnvVarOrThrow.js';

const sql = neon(mustGetEnv('POSTGRES_URL'));

export default sql;
