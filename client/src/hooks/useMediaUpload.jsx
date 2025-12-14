import { useCallback, useState } from "react";

export function useMediaUpload() {
  const uploadUrl = import.meta.env.VITE_MEDIA_UPLOAD_URL;
  const publicBaseUrl = import.meta.env.VITE_MEDIA_PUBLIC_BASE_URL ?? "";

  const [isUploading, setIsUploading] = useState(false);
  const [error, setError] = useState("");

  const uploadSongFile = useCallback(
    async (file) => {
      if (!file) return null;
      if (!uploadUrl) {
        setError("Missing VITE_MEDIA_UPLOAD_URL");
        return null;
      }

      setIsUploading(true);
      setError("");

      try {
        const fd = new FormData();
        fd.append("file", file);

        const res = await fetch(uploadUrl, {
          method: "POST",
          body: fd,
        });

        if (!res.ok) {
          const text = await res.text().catch(() => "");
          setError(text || `Upload failed (${res.status})`);
          return null;
        }

        const data = await res.json().catch(() => ({}));

        if (data?.url) return data.url;
        if (data?.path) {
          const base = publicBaseUrl.replace(/\/$/, "");
          const path = String(data.path).replace(/^\//, "");
          return base ? `${base}/${path}` : `/${path}`;
        }

        setError("Upload failed: missing url/path in response");
        return null;
      } catch (e) {
        setError(e?.message || "Upload failed");
        return null;
      } finally {
        setIsUploading(false);
      }
    },
    [uploadUrl, publicBaseUrl]
  );

  return { uploadSongFile, isUploading, error };
}
