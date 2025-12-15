import { createClient } from "@supabase/supabase-js";

const supabaseUrl = import.meta.env.VITE_SUPABASE_URL;
const supabaseAnonKey = import.meta.env.VITE_SUPABASE_ANON_KEY;

export const supabase = createClient(supabaseUrl ?? "", supabaseAnonKey ?? "");

export function assertSupabaseConfigured() {
  if (!supabaseUrl) return "Missing VITE_SUPABASE_URL";
  if (!supabaseAnonKey) return "Missing VITE_SUPABASE_ANON_KEY";
  return "";
}
