import { ArrowLeft, Image as ImageIcon, Save } from "lucide-react";
import { useEffect, useState } from "react";
import { toast } from "sonner";
import { api } from "../constants/api-client";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function ArtistEdit() {
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [artist, setArtist] = useState(null);

  const { navigateTo, getQuery } = useRoutingContext();

  const [form, setForm] = useState({
    name: "",
    bio: "",
    profileImage: "",
    spotifyId: "",
  });

  useEffect(() => {
    const fetchArtist = async () => {
      try {
        const data = await api.get(`/artists?id=${getQuery("id")}`);
        setArtist(data);
        console.log(data);
        setForm({
          name: data.name || "",
          bio: data.bio || "",
          profileImage: data.profileImage || "",
          spotifyId: data.spotifyId || "",
        });
      } catch {
        toast.error("Failed to load artist");
        navigateTo("artist", {});
      } finally {
        setIsLoading(false);
      }
    };

    fetchArtist();
  }, []);

  const handleSave = async () => {
    setIsSaving(true);
    try {
      const payload = {
        name: form.name.trim(),
        bio: form.bio.trim(),
        profileImage: form.profileImage.trim(),
        spotifyId: form.spotifyId.trim(),
      };

      await api.put(`/artists/?id=${artist.id}`, payload);
      toast.success("Artist updated successfully!");
      navigateTo("artist", {});
    } catch (err) {
      toast.error(err.response?.data?.message || "Failed to update artist");
    } finally {
      setIsSaving(false);
    }
  };

  if (isLoading) {
    return (
      <div className="flex min-h-screen items-center justify-center">
        <div className="h-12 w-12 animate-spin rounded-full border-4 border-primary border-t-transparent" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background py-12 px-4">
      <div className="mx-auto max-w-4xl">
        <div className="mb-10 flex items-center gap-4">
          <button
            onClick={() => navigateTo("artist")}
            className="rounded-full p-3 hover:bg-secondary transition-colors"
          >
            <ArrowLeft className="h-6 w-6" />
          </button>
          <h1 className="text-4xl font-bold">Edit Artist</h1>
        </div>

        <div className="mb-12 flex justify-center">
          <div className="relative">
            <div className="h-80 w-80 overflow-hidden rounded-full ring-8 ring-primary/10 shadow-2xl">
              <img
                src={`https://api.dicebear.com/7.x/avataaars/svg?seed=${form.name}`}
                alt={form.name}
                className="h-full w-full object-cover"
                onError={(e) => {
                  e.currentTarget.src = `https://api.dicebear.com/7.x/avataaars/svg?seed=${form.name}`;
                }}
              />
            </div>
            <div className="absolute bottom-6 right-6 rounded-full bg-primary p-4 text-white shadow-xl">
              <ImageIcon className="h-8 w-8" />
            </div>
          </div>
        </div>

        <div className="space-y-8 rounded-3xl bg-card p-10 shadow-xl">
          <div>
            <label className="mb-3 block text-lg font-semibold">Name</label>
            <input
              type="text"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              className="w-full rounded-xl border bg-secondary/30 px-6 py-4 text-lg focus:border-primary focus:outline-none focus:ring-4 focus:ring-primary/10"
            />
          </div>

          <div>
            <label className="mb-3 block text-lg font-semibold">Bio</label>
            <textarea
              rows={5}
              value={form.bio}
              onChange={(e) => setForm({ ...form, bio: e.target.value })}
              className="w-full rounded-xl border bg-secondary/30 px-6 py-4 focus:border-primary focus:outline-none focus:ring-4 focus:ring-primary/10"
            />
          </div>

          <div>
            <label className="mb-3 block text-lg font-semibold">
              Profile Image URL
            </label>
            <input
              type="text"
              value={form.profileImage}
              onChange={(e) =>
                setForm({ ...form, profile_image: e.target.value })
              }
              className="w-full rounded-xl border bg-secondary/30 px-6 py-4 font-mono text-sm focus:border-primary focus:outline-none focus:ring-4 focus:ring-primary/10"
            />
          </div>

          <div>
            <label className="mb-3 block text-lg font-semibold">
              Spotify ID
            </label>
            <input
              type="text"
              value={form.spotifyId}
              onChange={(e) => setForm({ ...form, spotify_id: e.target.value })}
              className="w-full rounded-xl border bg-secondary/30 px-6 py-4 font-mono text-sm focus:border-primary focus:outline-none focus:ring-4 focus:ring-primary/10"
            />
          </div>

          <div className="flex justify-end gap-4 pt-6">
            <button
              onClick={() => navigateTo("artist")}
              className="rounded-xl border border-input px-8 py-4 font-semibold hover:bg-secondary transition-colors"
            >
              Cancel
            </button>
            <button
              onClick={handleSave}
              disabled={isSaving}
              className="rounded-xl border border-input px-8 py-4 font-semibold hover:bg-secondary transition-colors flex items-center justify-between space-x-5"
            >
              <Save className="h-5 w-5" />
              {isSaving ? "Saving..." : "Save Changes"}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
