import { ArrowLeft, Clock, Music2, Play, Plus } from "lucide-react";
import SongItem from '../components/song-item';
import { useRoutingContext } from '../hooks/useRoutingContext';

function usePlaylistDetails(playlistId) {
    // Replace with your actual implementation
    return {
        playlist: {
            id: playlistId,
            name: "Chill Vibes",
            description: "Relaxing tracks for working and studying",
            created_at: "2024-01-15T10:30:00Z",
        },
        songs: [
            {
                id: "1",
                title: "Sunset Dreams",
                artistName: "Lo-Fi Master",
                artistId: 101,
                audioUrl: "https://example.com/audio1.mp3",
            },
            {
                id: "2",
                title: "Coffee Shop Beats",
                artistName: "Chill Producer",
                artistId: 102,
                audioUrl: "https://example.com/audio2.mp3",
            },
            {
                id: "3",
                title: "Morning Meditation",
                artistName: "Zen Sounds",
                artistId: 103,
                audioUrl: "https://example.com/audio3.mp3",
            },
            {
                id: "4",
                title: "Rainy Day Jazz",
                artistName: "Jazz Collective",
                artistId: 104,
            },
        ],
        isLoading: false,
        error: null,
    }
}

export default function PlaylistDetails() {
    const { playlist, songs, isLoading, error } = usePlaylistDetails(1)
    const { navigateTo } = useRoutingContext();

    const handlePlayRequest = (audioEl, songId) => {
        console.log("[v0] Play requested for song:", songId)
        audioEl.play()
    }

    const handlePauseRequest = (audioEl, songId) => {
        console.log("[v0] Pause requested for song:", songId)
        audioEl.pause()
    }

    const handleDelete = async (songId) => {
        console.log("[v0] Delete requested for song:", songId)
    }

    if (isLoading) {
        return (
            <div className="min-h-screen bg-white dark:bg-gray-950">
                <div className="container mx-auto px-4 py-8 max-w-5xl">
                    <div className="h-8 w-32 bg-gray-200 dark:bg-gray-800 animate-pulse rounded mb-8" />
                    <div className="h-48 bg-gray-200 dark:bg-gray-800 animate-pulse rounded-2xl mb-8" />
                    <div className="space-y-4">
                        {[...Array(4)].map((_, i) => (
                            <div key={i} className="h-32 bg-gray-200 dark:bg-gray-800 animate-pulse rounded-xl" />
                        ))}
                    </div>
                </div>
            </div>
        )
    }

    if (error) {
        return (
            <div className="min-h-screen bg-white dark:bg-gray-950 flex items-center justify-center">
                <div className="text-center">
                    <h2 className="text-2xl font-bold text-red-600 dark:text-red-400 mb-2">Error Loading Playlist</h2>
                    <p className="text-gray-600 dark:text-gray-400">{error.message || "Something went wrong"}</p>
                </div>
            </div>
        )
    }

    const totalSongs = songs?.length || 0
    const totalDuration = "24 min"

    return (
        <div className="min-h-screen container">
            <div className="mx-auto max-w-6xl">
                <button onClick={() => navigateTo('playlist')} className="inline-flex text-white items-center gap-2 dark:text-gray-400 hover:text-gray-900 dark:hover:text-gray-100 transition-colors mb-8">
                    <ArrowLeft className="h-5 w-5" />
                    <span className="font-medium">Back to Playlists</span>
                </button>

                <div className="bg-linear-to-br from-blue-500 to-purple-600 rounded-2xl p-8 mb-8 shadow-lg">
                    <div className="flex items-start gap-6">
                        <div className="h-32 w-32 rounded-xl bg-white/10 backdrop-blur-sm flex items-center justify-center border border-white/20 shadow-xl shrink-0">
                            <Music2 className="h-16 w-16 text-white" />
                        </div>

                        <div className="flex-1 text-white">
                            <p className="text-sm font-semibold uppercase tracking-wider opacity-90 mb-2">Playlist</p>
                            <h1 className="text-5xl font-bold mb-3 leading-tight">{playlist.name}</h1>
                            <p className="text-lg opacity-90 mb-4 leading-relaxed">{playlist.description}</p>

                            <div className="flex items-center gap-4 text-sm">
                                <span className="flex items-center gap-1.5">
                                    <Music2 className="h-4 w-4" />
                                    {totalSongs} {totalSongs === 1 ? "song" : "songs"}
                                </span>
                                <span className="flex items-center gap-1.5">
                                    <Clock className="h-4 w-4" />
                                    {totalDuration}
                                </span>
                                <span className="opacity-75">
                                    Created{" "}
                                    {new Date(playlist.created_at).toLocaleDateString("en-US", {
                                        month: "short",
                                        day: "numeric",
                                        year: "numeric",
                                    })}
                                </span>
                            </div>
                        </div>
                    </div>

                    <div className="flex items-center gap-3 mt-6">
                        <button className="inline-flex items-center gap-2 px-8 py-3 bg-white text-blue-600 font-bold rounded-full hover:scale-105 transition-transform shadow-lg">
                            <Play className="h-5 w-5 fill-current" />
                            Play All
                        </button>
                        <button className="inline-flex items-center gap-2 px-6 py-3 bg-white/10 backdrop-blur-sm text-white font-semibold rounded-full border border-white/20 hover:bg-white/20 transition-colors">
                            <Plus className="h-5 w-5" />
                            Add Song
                        </button>
                    </div>
                </div>

                <div>
                    <h2 className="text-2xl font-bold text-gray-900 dark:text-gray-100 mb-6">Songs</h2>

                    {songs && songs.length > 0 ? (
                        <ul className="space-y-4">
                            {songs.map((song, index) => (
                                <SongItem
                                    key={song.id}
                                    song={song}
                                    isActive={false}
                                    onPlayRequest={handlePlayRequest}
                                    onPauseRequest={handlePauseRequest}
                                    onDelete={handleDelete}
                                />
                            ))}
                        </ul>
                    ) : (
                        <div className="text-center py-16 bg-gray-50 dark:bg-gray-900 rounded-xl border-2 border-dashed border-gray-300 dark:border-gray-700">
                            <div className="w-16 h-16 bg-gray-200 dark:bg-gray-800 rounded-full flex items-center justify-center mx-auto mb-4">
                                <Music2 className="w-8 h-8 text-gray-400 dark:text-gray-600" />
                            </div>
                            <h3 className="text-xl font-semibold text-gray-900 dark:text-gray-100 mb-2">No Songs Yet</h3>
                            <p className="text-gray-600 dark:text-gray-400 mb-6">Add your first song to this playlist</p>
                            <button className="inline-flex items-center gap-2 px-6 py-3 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors">
                                <Plus className="w-5 h-5" />
                                Add Song
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}
