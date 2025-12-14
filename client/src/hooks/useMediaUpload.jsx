import { useCallback, useState } from "react";
import {
  assertSupabaseConfigured,
  supabase,
} from "../constants/supabase-client";

function getFileExtension(filename) {
  const idx = String(filename || "").lastIndexOf(".");
  return idx >= 0
    ? String(filename)
        .slice(idx + 1)
        .toLowerCase()
    : "";
}

function makeId() {
  if (globalThis.crypto?.randomUUID) return globalThis.crypto.randomUUID();
  return `${Date.now()}-${Math.random().toString(16).slice(2)}`;
}

export function useMediaUpload() {
  const bucket = import.meta.env.VITE_SUPABASE_BUCKET;
  const folder = import.meta.env.VITE_SUPABASE_FOLDER ?? "songs";

  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState("");

  const uploadSongFile = useCallback(
    async (file) => {
      if (!file) return null;

      const cfgErr = assertSupabaseConfigured();
      if (cfgErr) {
        setError(cfgErr);
        return null;
      }

      if (!bucket) {
        setError("Missing VITE_SUPABASE_BUCKET");
        return null;
      }

      setIsUploading(true);
      setError("");

      try {
        const ext = getFileExtension(file.name);
        const safeExt = ext ? `.${ext}` : "";
        const objectPath = `${String(folder).replace(
          /(^\/|\/$)/g,
          ""
        )}/${makeId()}${safeExt}`;

        const { error: uploadError } = await supabase.storage
          .from(bucket)
          .upload(objectPath, file, {
            contentType: file.type || undefined,
            upsert: false,
          });

        if (uploadError) {
          setError(uploadError.message || "Upload failed");
          return null;
        }

        const publicRes = supabase.storage
          .from(bucket)
          .getPublicUrl(objectPath);
        const publicUrl = publicRes?.data?.publicUrl;
        if (publicUrl) return publicUrl;

        const { data: signedData, error: signedErr } = await supabase.storage
          .from(bucket)
          .createSignedUrl(objectPath, 60 * 60);

        if (signedErr) {
          setError(
            signedErr.message ||
              "Upload succeeded but URL creation failed (make the bucket public, or allow signed URLs)"
          );
          return null;
        }

        return signedData?.signedUrl || null;
      } catch (e) {
        setError(e?.message || "Upload failed");
        return null;
      } finally {
        setIsUploading(false);
      }
    },
    [bucket, folder]
  );

  return { uploadSongFile, isUploading, error };
}
