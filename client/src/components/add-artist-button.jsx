import { Plus, X } from 'lucide-react';
import React from 'react';
import { useRoutingContext } from '../context/RoutingContext';
import { useArtist } from '../hooks/useArtist';

export default function AddArtistButton() {
    const { currentRoute } = useRoutingContext();
    const [isOpen, setIsOpen] = React.useState(false);
    const [form, setForm] = React.useState({
        name: '',
        bio: '',
        profile_image: '',
        spotify_id: '',
    });
    const { createArtist } = useArtist();

    const handleSubmit = async () => {
        const artist = {
            name: form.name || randomName(),
            bio: form.bio || randomBio(),
            profile_image: form.profile_image || randomImage1313(),
            spotify_id: form.spotify_id || randomUUID(),
        };

        await createArtist(artist);

        setIsOpen(false);
        setForm({ name: '', bio: '', profile_image: '', spotify_id: '' });
    };

    if (currentRoute !== 'artist') return null;

    return (
        <>
            <button className='!rounded-full !bg-primary h-9 flex items-center justify-between'
                onClick={() => setIsOpen(true)}
            >
                <Plus />
            </button>

            {isOpen && (
                <div className="fixed inset-0 mt-96 z-50 flex items-center justify-center p-4">
                    <div
                        className="absolute inset-0 bg-black/50 backdrop-blur-sm"
                        onClick={() => setIsOpen(false)}
                    />

                    <div className="relative w-full max-w-lg rounded-2xl bg-background p-6 px-8 shadow-2xl border-2 border-gray-500">
                        <div className="mb-6 flex items-center justify-between">
                            <h2 className="text-2xl font-bold">Add New Artist</h2>
                            <button
                                onClick={() => setIsOpen(false)}
                                className="rounded-full p-2 hover:bg-secondary"
                            >
                                <X className="h-5 w-5" />
                            </button>
                        </div>

                        <div className="space-y-5">
                            <div>
                                <label className="mb-1 block text-md font-bold">Name</label>
                                <input
                                    type="text"
                                    value={form.name}
                                    onChange={(e) => setForm({ ...form, name: e.target.value })}
                                    className="w-full rounded-lg border bg-secondary/50 px-4 py-3 text-foreground placeholder:text-muted-foreground focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                                />
                            </div>

                            <div>
                                <label className="mb-1 block text-md font-medium">Bio</label>
                                <textarea
                                    rows={4}
                                    value={form.bio}
                                    onChange={(e) => setForm({ ...form, bio: e.target.value })}
                                    className="w-full rounded-lg border bg-secondary/50 px-4 py-3 text-foreground placeholder:text-muted-foreground focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                                />
                            </div>

                            <div>
                                <label className="mb-1 block text-md font-medium">Profile Image URL</label>
                                <input
                                    type="text"
                                    value={form.profile_image}
                                    onChange={(e) => setForm({ ...form, profile_image: e.target.value })}
                                    className="w-full rounded-lg border bg-secondary/50 px-4 py-3 text-foreground placeholder:text-muted-foreground focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                                />
                            </div>

                            <div>
                                <label className="mb-1 block text-md font-medium">Spotify ID</label>
                                <input
                                    type="text"
                                    value={form.spotify_id}
                                    onChange={(e) => setForm({ ...form, spotify_id: e.target.value })}
                                    className="w-full rounded-lg border bg-secondary/50 px-4 py-3 text-foreground placeholder:text-muted-foreground focus:border-primary focus:outline-none focus:ring-2 focus:ring-primary/20"
                                />
                            </div>
                        </div>

                        <div className="mt-8 flex justify-end gap-3">
                            <button
                                onClick={() => setIsOpen(false)}
                                className="rounded-lg border border-input px-5 py-2.5 font-medium hover:bg-secondary"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={handleSubmit}
                                className="rounded-lg border border-input px-5 py-2.5 font-medium hover:bg-secondary"
                            >
                                Add Artist
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </>
    );
}