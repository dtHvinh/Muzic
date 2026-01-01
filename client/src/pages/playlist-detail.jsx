import dayjs from "dayjs";
import {ArrowLeft, Music2, Plus} from "lucide-react";
import SongItem from "../components/song-item";
import usePlaylist from "../hooks/usePlaylist";
import usePlaylistDetails from "../hooks/usePlaylistDetails";
import {useRoutingContext} from "../hooks/useRoutingContext";

export default function PlaylistDetails() {
    const {navigateTo, getQuery} = useRoutingContext();
    const playlistId = getQuery("id");
    const {playlist, songs, isLoading, error, refetch} = usePlaylistDetails(playlistId);
    const {removePlaylistItem} = usePlaylist();

    const handlePlayRequest = (audioEl, songId) => {
        console.log("[v0] Play requested for song:", songId);
        audioEl.play();
    };

    const handlePauseRequest = (audioEl, songId) => {
        console.log("[v0] Pause requested for song:", songId);
        audioEl.pause();
    };

    const handleDelete = async (songId) => {
        console.log("[v0] Delete requested for song:", songId);
    };

    if (isLoading) {
        return (
            <div className="min-h-screen container">
                <div className="mx-auto max-w-6xl">
                    <div className="inline-flex items-center gap-2 mb-8">
                        <div className="h-5 w-5 bg-gray-200 dark:bg-gray-800 animate-pulse rounded"/>
                        <div className="h-5 w-40 bg-gray-200 dark:bg-gray-800 animate-pulse rounded"/>
                    </div>

                    <div className="rounded-2xl p-8 mb-8 bg-gray-200 dark:bg-gray-800 animate-pulse">
                        <div className="flex items-start gap-6">
                            <div className="h-32 w-32 rounded-xl bg-gray-300 dark:bg-gray-700"/>

                            <div className="flex-1">
                                <div className="h-4 w-20 rounded bg-gray-300 dark:bg-gray-700 mb-3"/>
                                <div className="h-10 w-2/3 rounded bg-gray-300 dark:bg-gray-700 mb-4"/>
                                <div className="h-5 w-3/4 rounded bg-gray-300 dark:bg-gray-700 mb-6"/>

                                <div className="flex items-center gap-4">
                                    <div className="h-4 w-24 rounded bg-gray-300 dark:bg-gray-700"/>
                                    <div className="h-4 w-20 rounded bg-gray-300 dark:bg-gray-700"/>
                                    <div className="h-4 w-36 rounded bg-gray-300 dark:bg-gray-700"/>
                                </div>
                            </div>
                        </div>

                        <div className="flex items-center gap-3 mt-6">
                            <div className="h-12 w-32 rounded-full bg-gray-300 dark:bg-gray-700"/>
                            <div className="h-12 w-28 rounded-full bg-gray-300 dark:bg-gray-700"/>
                        </div>
                    </div>

                    <div>
                        <div className="h-7 w-24 bg-gray-200 dark:bg-gray-800 animate-pulse rounded mb-6"/>
                        <div className="space-y-4">
                            {[...Array(6)].map((_, i) => (
                                <div
                                    key={i}
                                    className="h-24 bg-gray-200 dark:bg-gray-800 animate-pulse rounded-xl"
                                />
                            ))}
                        </div>
                    </div>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen bg-white dark:bg-gray-950 flex items-center justify-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold text-red-600 dark:text-red-400 mb-2">
                        Error Loading Playlist
                    </h2>
                    <p className="text-gray-600 dark:text-gray-400">
                        {error || "Something went wrong"}
                    </p>
                </div>
            </div>
        );
    }

    const totalSongs = songs?.length || 0;
    const totalDuration = "24 min";

    return (
        <div className="min-h-screen container">
            <div className="mx-auto max-w-6xl">
                <button
                    onClick={() => navigateTo("playlist")}
                    className="inline-flex text-white items-center gap-2 hover:text-gray-500 transition-colors mb-8"
                >
                    <ArrowLeft className="h-5 w-5"/>
                    <span className="font-medium">Back to Playlists</span>
                </button>

                <div className="bg-linear-to-br from-blue-500 to-purple-600 rounded-2xl p-8 mb-8 shadow-lg">
                    <div className="flex items-start gap-6">
                        <div
                            className="h-32 w-32 rounded-xl bg-white/10 backdrop-blur-sm flex items-center justify-center border border-white/20 shadow-xl shrink-0">
                            <Music2 className="h-16 w-16 text-white"/>
                        </div>

                        <div className="flex-1 text-white">
                            <p className="text-sm font-semibold uppercase tracking-wider opacity-90 mb-2">
                                Playlist
                            </p>
                            <h1 className="text-5xl font-bold mb-3 leading-tight">
                                {playlist?.name}
                            </h1>
                            <p className="text-lg opacity-90 mb-4 leading-relaxed">
                                {playlist?.description}
                            </p>

                            <div className="flex items-center gap-4 text-sm">
                <span className="flex items-center gap-1.5">
                  <Music2 className="h-4 w-4"/>
                    {totalSongs} {totalSongs === 1 ? "song" : "songs"}
                </span>
                                <span className="opacity-75">
                  Created{" "}
                                    {dayjs(playlist?.createdAt).fromNow()}
                </span>
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <h2 className="text-2xl font-bold text-gray-900 dark:text-gray-100 mb-6">
                        Songs
                    </h2>

                    {songs && songs.length > 0 ? (
                        <ul className="space-y-4">
                            {songs.map((song) => (
                                <SongItem
                                    key={song.id}
                                    song={song}
                                    isActive={false}
                                    onPlayRequest={handlePlayRequest}
                                    onPauseRequest={handlePauseRequest}
                                    onDelete={handleDelete}
                                    onRemoveFromPlaylist={() => {
                                        removePlaylistItem(playlistId, song.id)
                                        refetch()
                                    }}
                                />
                            ))}
                        </ul>
                    ) : (
                        <div
                            className="text-center py-16 bg-gray-50 dark:bg-gray-900 rounded-xl border-2 border-dashed border-gray-300 dark:border-gray-700">
                            <div
                                className="w-16 h-16 bg-gray-200 dark:bg-gray-800 rounded-full flex items-center justify-center mx-auto mb-4">
                                <Music2 className="w-8 h-8 text-gray-400 dark:text-gray-600"/>
                            </div>
                            <h3 className="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2">
                                No Songs Yet
                            </h3>
                            <p className="text-gray-600 dark:text-gray-400 mb-6">
                                Add your first song to this playlist
                            </p>
                            <button
                                onClick={() => navigateTo("song")}
                                className="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors">
                                <Plus className="w-5 h-5"/>
                                Add Song
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
}
