import {useRoutingContext} from "@/hooks/useRoutingContext.jsx";
import {useSearchArtist} from "@/hooks/useSearchArtist.js";
import {useSongs} from "@/hooks/useSongs.js";
import SubLayout from "@/layouts/sublayout.jsx";
import ArtistCard, {SkeletonArtistCard} from "@/components/artist-card.jsx";
import {useArtist} from "@/hooks/useArtist.js";
import usePlaylist from "@/hooks/usePlaylist.js";
import {useSong} from "@/hooks/useSong.js";
import React from "react";
import SongItem from "@/components/song-item.jsx";

export default function Search() {
    const {getQuery} = useRoutingContext()

    const {artists, isLoading: artistLoading, reset} = useSearchArtist({
        query: getQuery('term'),
        offset: 0,
        limit: 6,
    })
    const {deleteArtist} = useArtist();

    const {songs, isLoading, refetch} = useSongs({
        title: getQuery('term'),
        limit: 50,
        offset: 0
    })
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


    const q = getQuery('term') ?? "";
    return (
        <SubLayout>
            <section>
                <h3 className="text-xl font-bold">
                    {`Artists matching "${q}"`}
                </h3>

                <div className="mt-5 grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6">
                    {artistLoading &&
                        Array.from({length: 7}).map((_, i) => (
                            <SkeletonArtistCard key={i}/>
                        ))}

                    {!artistLoading && artists?.map((artist) => (
                        <ArtistCard
                            key={artist.id}
                            artist={artist}
                            cardProps={{size: 24}}
                            onDelete={async () => {
                                if (confirm("Do you want to delete this artist?")) {
                                    await deleteArtist(artist.id)
                                    reset()
                                }
                            }}
                        />
                    ))}
                </div>

                {!artistLoading && artists?.length === 0 && (
                    <p className="mt-5 text-center text-gray-400">
                        No artists found
                    </p>
                )}
            </section>

            <section className="mt-12">
                <h3 className="text-xl font-bold">
                    {`Songs matching "${q}"`}
                </h3>

                {isLoading && (
                    <p className="mt-5 text-center text-muted-foreground">
                        Loading songs…
                    </p>
                )}

                {!isLoading && songs.length === 0 && (
                    <p className="mt-5 text-center text-muted-foreground">
                        No songs found.
                    </p>
                )}

                {!isLoading && songs.length > 0 && (
                    <ul className="mt-5 space-y-4">
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
                )}
            </section>
        </SubLayout>
    )

}