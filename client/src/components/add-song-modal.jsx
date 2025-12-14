import { X } from "lucide-react";
import React from "react";
import { useAddSongModal } from "../hooks/useAddSongModal";
import { useMediaUpload } from "../hooks/useMediaUpload";
import { useSong } from "../hooks/useSong";
import ArtistSelect from "./artist-select";

export default function AddSongModal({ open, onClose }) {
  const [form, setForm] = React.useState({
    title: "",
    artist_id: "",
    spotify_id: "",
  });
  const [artistError, setArtistError] = React.useState("");
  const [audioFile, setAudioFile] = React.useState(null);

  const {
    uploadSongFile,
    isUploading: isUploadingFile,
    error: uploadError,
  } = useMediaUpload();

  const { createSong, isLoading } = useSong();
  const { fireAllEvents } = useAddSongModal();

  const handleClose = () => {
    onClose?.();
    setForm({ title: "", artist_id: "", spotify_id: "" });
    setArtistError("");
    setAudioFile(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!form.artist_id) {
      setArtistError("Please choose an artist");
      return;
    }
    setArtistError("");
    let audioUrl;
    if (audioFile) {
      audioUrl = await uploadSongFile(audioFile);
      if (!audioUrl) {
        return;
      }
    }
    const payload = {
      title: form.title.trim(),
      artist_id: Number(form.artist_id),
      spotify_id: form.spotify_id.trim() || undefined,
    };
    if (audioUrl) {
      payload.audio_url = audioUrl;
    }
    const res = await createSong(payload);
    if (res) {
      fireAllEvents();
      handleClose();
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center px-4 py-10">
      <div
        className="absolute inset-0 bg-linear-to-br from-black/70 via-black/60 to-black/80 backdrop-blur-lg"
        onClick={handleClose}
      />

      <div className="relative w-full max-w-2xl overflow-hidden rounded-3xl border border-white/10 bg-linear-to-br from-[#111016] via-[#121422] to-[#0f131c] shadow-[0_30px_80px_-40px_rgba(59,130,246,0.45)]">
        <div className="absolute -top-24 -right-12 h-56 w-56 rounded-full bg-primary/20 blur-3xl" />
        <div className="absolute -bottom-20 -left-16 h-48 w-48 rounded-full bg-secondary/30 blur-3xl" />

        <div className="relative flex flex-col gap-8 p-10">
          <header className="flex items-start justify-between gap-6">
            <div>
              <p className="text-xs uppercase tracking-[0.35em] text-muted-foreground/70">
                Song Management
              </p>
              <h2 className="mt-2 text-3xl font-semibold text-foreground">
                Add New Song
              </h2>
              <p className="mt-2 max-w-xl text-sm text-muted-foreground">
                Link the track to an existing artist and optionally include its
                Spotify ID for richer metadata.
              </p>
            </div>
            <button
              onClick={handleClose}
              className="rounded-full border border-white/10 p-2 text-muted-foreground transition-colors duration-150 hover:border-white/30 hover:text-foreground"
              aria-label="Close"
            >
              <X className="h-5 w-5" />
            </button>
          </header>

          <form className="grid gap-6" onSubmit={handleSubmit}>
            <label className="group flex flex-col gap-2">
              <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                Title
              </span>
              <input
                type="text"
                value={form.title}
                onChange={(e) => setForm({ ...form, title: e.target.value })}
                className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15"
                placeholder="eg. Let It Happen"
                required
              />
            </label>

            <ArtistSelect
              value={form.artist_id}
              onChange={(artistId) => {
                setForm({ ...form, artist_id: artistId });
                setArtistError("");
              }}
              error={artistError}
            />

            <label className="group flex flex-col gap-2">
              <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                Spotify Track ID
              </span>
              <input
                type="text"
                value={form.spotify_id}
                onChange={(e) =>
                  setForm({ ...form, spotify_id: e.target.value })
                }
                className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15"
                placeholder="Optional but recommended"
              />
            </label>

            <label className="group flex flex-col gap-2">
              <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                Audio File
              </span>
              <input
                type="file"
                accept="audio/*"
                onChange={(e) => setAudioFile(e.target.files?.[0] ?? null)}
                className="rounded-2xl border border-dashed border-white/10 bg-transparent px-4 py-6 text-sm text-foreground shadow-inner transition hover:border-primary/60 focus:border-primary/60 focus:outline-none"
              />
              {audioFile && (
                <span className="text-xs text-muted-foreground">
                  Selected: {audioFile.name}
                </span>
              )}
              {uploadError && (
                <span className="text-xs text-red-400">{uploadError}</span>
              )}
            </label>

            <div className="flex flex-col-reverse gap-3 md:flex-row md:items-center md:justify-between">
              <button
                type="button"
                onClick={handleClose}
                className="w-full rounded-2xl border border-white/10 px-5 py-3 text-sm font-medium text-muted-foreground transition hover:border-white/30 hover:text-foreground md:w-auto"
                disabled={isLoading}
              >
                Cancel
              </button>

              <button
                type="submit"
                className="w-full rounded-2xl bg-primary! px-6 py-3 text-sm font-semibold shadow-[0_15px_40px_-15px_rgba(56,189,248,0.55)] transition-transform duration-200 hover:-translate-y-0.5 hover:shadow-[0_20px_45px_-20px_rgba(56,189,248,0.7)] disabled:translate-y-0 disabled:opacity-60 disabled:shadow-none md:w-auto"
                disabled={isLoading || isUploadingFile}
              >
                {isLoading || isUploadingFile ? "Saving song..." : "Add Song"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
