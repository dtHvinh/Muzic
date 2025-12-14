import React from "react";
import AddSongButton from "../components/add-song-button";
import SongItem from "../components/song-item";
import { useSongs } from "../hooks/useSongs";

export default function Songs() {
  const { songs, isLoading, refetch } = useSongs({ limit: 50, offset: 0 });
  const activeAudioRef = React.useRef(null);
  const [playingSongId, setPlayingSongId] = React.useState(null);

  const handlePlayRequest = React.useCallback((audioEl, songId) => {
    if (!audioEl) return;

    if (activeAudioRef.current && activeAudioRef.current !== audioEl) {
      activeAudioRef.current.pause();
    }

    activeAudioRef.current = audioEl;
    setPlayingSongId(songId);

    audioEl.play().catch(() => {});
  }, []);

  const handlePauseRequest = React.useCallback((audioEl, songId) => {
    if (audioEl) audioEl.pause();
    setPlayingSongId((cur) => (cur === songId ? null : cur));
  }, []);

  return (
    <div className="space-y-6">
      <AddSongButton onAdd={refetch} />
      {isLoading && (
        <p className="text-center text-muted-foreground">Loading songs…</p>
      )}
      {!isLoading && songs.length === 0 && (
        <p className="text-center text-muted-foreground">No songs yet.</p>
      )}
      <ul className="space-y-4 grid grid-cols-6 space-x-5">
        {songs.map((song) => (
          <SongItem
            key={song.id}
            song={song}
            isActive={playingSongId === song.id}
            onPlayRequest={handlePlayRequest}
            onPauseRequest={handlePauseRequest}
          />
        ))}
      </ul>
    </div>
  );
}
