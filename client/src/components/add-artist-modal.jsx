import { X } from "lucide-react";
import React from "react";
import { useAddArtistModal } from "../hooks/useAddArtistModal";
import { useArtist } from "../hooks/useArtist";

export default function AddArtistModal({ open, onClose }) {
  const [form, setForm] = React.useState({
    name: "",
    bio: "",
    profile_image: "",
    spotify_id: "",
  });

  const { createArtist, isLoading } = useArtist();
  const { fireAllEvents } = useAddArtistModal();

  const handleClose = () => {
    onClose?.();
    setForm({ name: "", bio: "", profile_image: "", spotify_id: "" });
  };

  const handleSubmit = async (event) => {
    event?.preventDefault();

    const artist = {
      name: form.name,
      bio: form.bio,
      profile_image: form.profile_image,
      spotify_id: form.spotify_id,
    };

    const created = await createArtist(artist);
    if (created) {
      handleClose();
      fireAllEvents();
    }
  };

  if (!open) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center px-4 py-10">
      <div
        className="absolute inset-0 bg-linear-to-br from-black/70 via-black/60 to-black/80 backdrop-blur-lg"
        onClick={handleClose}
      />

      <div
        className="relative w-full max-w-2xl overflow-hidden rounded-3xl border border-white/10 bg-linear-to-br from-[#111016] via-[#151622] to-[#10121a] shadow-[0_30px_80px_-40px_rgba(56,189,248,0.45)] transition-all duration-200"
        role="dialog"
        aria-modal="true"
      >
        <div className="absolute -top-24 -right-10 h-56 w-56 rounded-full bg-primary/30 blur-3xl" />
        <div className="absolute -bottom-20 -left-16 h-48 w-48 rounded-full bg-secondary/40 blur-3xl" />

        <div className="relative flex flex-col gap-10 p-10">
          <header className="flex items-start justify-between gap-6">
            <div>
              <p className="text-xs uppercase tracking-[0.35em] text-muted-foreground/70">
                Artist Management
              </p>
              <h2 className="mt-2 text-3xl font-semibold text-foreground">
                Add New Artist
              </h2>
              <p className="mt-2 max-w-xl text-sm text-muted-foreground">
                Provide a short bio and Spotify ID so the artist profile is
                ready to surface in search and playlists.
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
            <div className="grid gap-6 md:grid-cols-2">
              <label className="group flex flex-col gap-2">
                <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                  Name
                </span>
                <input
                  type="text"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15"
                  placeholder="eg. Tame Impala"
                  required
                />
              </label>

              <label className="group flex flex-col gap-2">
                <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                  Spotify ID
                </span>
                <input
                  type="text"
                  value={form.spotify_id}
                  onChange={(e) =>
                    setForm({ ...form, spotify_id: e.target.value })
                  }
                  className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15"
                  placeholder="Spotify artist ID"
                />
              </label>
            </div>

            <label className="group flex flex-col gap-2">
              <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                Profile Image URL
              </span>
              <input
                type="text"
                value={form.profile_image}
                onChange={(e) =>
                  setForm({ ...form, profile_image: e.target.value })
                }
                className="rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15"
                placeholder="https://image.host/artist.png"
              />
            </label>

            <label className="group flex flex-col gap-2">
              <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
                Bio
              </span>
              <textarea
                rows={4}
                value={form.bio}
                onChange={(e) => setForm({ ...form, bio: e.target.value })}
                className="resize-none rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm leading-relaxed text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15"
                placeholder="Share a quick introduction or notable achievements."
              />
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
                disabled={isLoading}
              >
                {isLoading ? "Adding artist..." : "Add Artist"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
