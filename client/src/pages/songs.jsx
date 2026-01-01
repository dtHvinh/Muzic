import React from "react";
import AddSongButton from "../components/add-song-button";
import SongItem from "../components/song-item";
import usePlaylist from "../hooks/usePlaylist";
import {useSong} from "../hooks/useSong";
import {useSongs} from "../hooks/useSongs";
import SubLayout from "../layouts/sublayout";

export default function Songs() {
    const {songs, isLoading, refetch} = useSongs({title: '', limit: 50, offset: 0});
    const {addPlaylistItem} = usePlaylist();
    const {deleteSong} = useSong();

    const activeAudioRef = React.useRef(null);
    const [playingSongId, setPlayingSongId] = React.useState(null);

    const handlePlayRequest = React.useCallback((audioEl, songId) => {
        if (!audioEl) return;

        if (activeAudioRef.current && activeAudioRef.current !== audioEl) {
            activeAudioRef.current.pause();
        }

        activeAudioRef.current = audioEl;
        setPlayingSongId(songId);

        audioEl.play().catch(() => {
        });
    }, []);

    const handlePauseRequest = React.useCallback((audioEl, songId) => {
        if (audioEl) audioEl.pause();
        setPlayingSongId((cur) => (cur === songId ? null : cur));
    }, []);

    const handleDelete = React.useCallback(
        async (songId) => {
            if (playingSongId === songId) {
                activeAudioRef.current?.pause?.();
                activeAudioRef.current = null;
                setPlayingSongId(null);
            }

            const ok = await deleteSong(songId);
            if (ok) refetch();
        },
        [deleteSong, refetch, playingSongId]
    );

    return (
        <SubLayout>
            <div className="flex items-center justify-between mb-8">
                <div>
                    <h1 className="text-4xl font-bold text-white mb-2">Songs</h1>
                    <p className="text-gray-600 dark:text-gray-600">Explore your music</p>
                </div>
                <AddSongButton onAdd={refetch}/>
            </div>
            <div className="space-y-6">
                {isLoading && (
                    <p className="text-center text-muted-foreground">Loading songs…</p>
                )}

                {!isLoading && songs.length === 0 && (
                    <p className="text-center text-muted-foreground">No songs yet.</p>
                )}

                <ul className="space-y-4 grid grid-cols-1 space-x-5">
                    {songs.map((song) => (
                        <SongItem
                            key={song.id}
                            song={song}
                            isActive={playingSongId === song.id}
                            onPlayRequest={handlePlayRequest}
                            onPauseRequest={handlePauseRequest}
                            onDelete={handleDelete}
                            onAddToPlaylist={(songId, playlistId) =>
                                addPlaylistItem(playlistId, songId)
                            }
                        />
                    ))}
                </ul>
            </div>
        </SubLayout>
    );
}
