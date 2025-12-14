import { X } from "lucide-react";
import React from "react";
import { useSong } from "../hooks/useSong";
import { useAddSongModal } from "../hooks/useAddSongModal";

export default function AddSongModal({ open, onClose }) {
  const [form, setForm] = React.useState({
    title: "",
    artist_id: "",
    spotify_id: "",
    duration_ms: "",
  });

  const { createSong, isLoading } = useSong();
  const { fireAllEvents } = useAddSongModal();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const payload = {
      title: form.title.trim(),
      artist_id: Number(form.artist_id),
      spotify_id: form.spotify_id.trim() || undefined,
      duration_ms: form.duration_ms ? Number(form.duration_ms) : undefined,
    };
    const res = await createSong(payload);
    if (res) {
      fireAllEvents();
      onClose?.();
      setForm({
        title: "",
        artist_id: "",
        spotify_id: "",
        duration_ms: "",
      });
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center px-4 py-10">
      <div className="absolute inset-0 bg-black/70" onClick={onClose} />
      <div className="relative w-full max-w-2xl rounded-3xl bg-card p-8">
        <header className="mb-6 flex items-center justify-between">
          <div>
            <p className="text-xs uppercase tracking-[0.35em] text-muted-foreground">
              Song Management
            </p>
            <h2 className="text-3xl font-semibold">Add Song</h2>
          </div>
          <button
            onClick={onClose}
            className="rounded-full border border-white/10 p-2"
          >
            <X className="h-5 w-5" />
          </button>
        </header>

        <form className="space-y-4" onSubmit={handleSubmit}>
          <input
            className="w-full rounded-xl border bg-secondary/20 px-4 py-3"
            placeholder="Title"
            value={form.title}
            onChange={(e) => setForm({ ...form, title: e.target.value })}
            required
          />
          <input
            className="w-full rounded-xl border bg-secondary/20 px-4 py-3"
            placeholder="Artist ID"
            value={form.artist_id}
            onChange={(e) => setForm({ ...form, artist_id: e.target.value })}
            required
          />
          <div className="grid gap-4 md:grid-cols-2">
            <input
              className="rounded-xl border bg-secondary/20 px-4 py-3"
              placeholder="Spotify ID"
              value={form.spotify_id}
              onChange={(e) => setForm({ ...form, spotify_id: e.target.value })}
            />
            <input
              className="rounded-xl border bg-secondary/20 px-4 py-3"
              placeholder="Duration (ms)"
              value={form.duration_ms}
              onChange={(e) =>
                setForm({ ...form, duration_ms: e.target.value })
              }
            />
          </div>

          <div className="flex justify-end gap-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="rounded-xl border px-6 py-3"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className="rounded-xl bg-primary px-6 py-3 text-primary-foreground disabled:opacity-60"
            >
              {isLoading ? "Saving..." : "Add Song"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
