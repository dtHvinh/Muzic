import AddSongButton from "../components/add-song-button";
import { useSongs } from "../hooks/useSongs";

export default function Songs() {
  const { songs, isLoading, refetch } = useSongs({ limit: 50, offset: 0 });

  return (
    <div className="space-y-6">
      <AddSongButton onAdd={refetch} />
      {isLoading && <p>Loading songs…</p>}
      {!isLoading && songs.length === 0 && (
        <p className="text-muted-foreground">No songs yet.</p>
      )}
      <ul className="space-y-4">
        {songs.map((song) => (
          <li key={song.id} className="rounded-2xl bg-secondary/30 p-4">
            <p className="text-lg font-semibold">{song.title}</p>
            <p className="text-sm text-muted-foreground">
              Artist ID: {song.artistId} • Duration: {song.durationMs ?? "—"} ms
            </p>
            {song.notes && <p className="text-sm mt-2">{song.notes}</p>}
          </li>
        ))}
      </ul>
    </div>
  );
}
