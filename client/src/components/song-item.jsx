import { ListMusic, Music, Pause, Play, Trash2 } from "lucide-react";
import React from "react";
import usePlaylists from '../hooks/usePlaylists';

function formatTime(seconds) {
  const s = Number.isFinite(seconds) ? Math.max(0, seconds) : 0;
  const m = Math.floor(s / 60);
  const r = Math.floor(s % 60);
  return `${m}:${String(r).padStart(2, "0")}`;
}

export default function SongItem({
  song,
  isActive,
  onPlayRequest,
  onPauseRequest,
  onDelete,
  onAddToPlaylist
}) {
  const audioRef = React.useRef(null)

  const [isPlaying, setIsPlaying] = React.useState(false)
  const [duration, setDuration] = React.useState(0)
  const [currentTime, setCurrentTime] = React.useState(0)
  const [isDeleting, setIsDeleting] = React.useState(false)
  const [selectedPlaylist, setSelectedPlaylist] = React.useState("");

  const { playlists } = usePlaylists({
    limit: 50,
    offset: 0
  })

  React.useEffect(() => {
    if (!isActive && audioRef.current) {
      audioRef.current.pause()
    }
  }, [isActive])

  const hasAudio = Boolean(song?.audioUrl)
  const artistLabel = song?.artistName || (song?.artistId != null ? `Artist #${song.artistId}` : "Unknown artist")

  const togglePlay = async () => {
    if (!hasAudio) return

    const el = audioRef.current
    if (!el) return

    if (isPlaying) {
      onPauseRequest?.(el, song.id)
      return
    }

    onPlayRequest?.(el, song.id)
  }

  const handleDelete = async (e) => {
    e?.stopPropagation?.()
    if (!onDelete || isDeleting) return

    const ok = confirm(`Delete "${song?.title ?? "this song"}"?`)
    if (!ok) return

    setIsDeleting(true)
    try {
      await onDelete(song.id)
    } finally {
      setIsDeleting(false)
    }
  }

  const handleAddToPlaylist = (e) => {
    e.stopPropagation();

    const playlistId = Number(e.target.value);
    if (!playlistId) return;

    onAddToPlaylist?.(song.id, playlistId);
    setSelectedPlaylist("");
  };

  return (
    <li className="group w-full relative overflow-hidden rounded-xl bg-linear-to-br from-gray-50 to-gray-100 dark:from-gray-800/40 dark:to-gray-900/40 border border-gray-200 dark:border-gray-700/50 transition-all duration-300 hover:border-gray-300 dark:hover:border-gray-600 hover:shadow-lg">
      <div className="p-5">
        <div className="flex items-center gap-4">
          <div className="relative shrink-0">
            <div className="h-16 w-16 rounded-xl bg-linear-to-br from-blue-500/20 to-purple-500/10 dark:from-blue-400/20 dark:to-purple-400/10 flex items-center justify-center border border-gray-300 dark:border-gray-600 shadow-sm">
              <Music className="h-7 w-7 text-blue-600 dark:text-blue-400" />
            </div>
            {isPlaying && <div className="absolute inset-0 rounded-xl bg-blue-500/20 animate-pulse" />}
          </div>

          <div className="flex-1 min-w-0">
            <h3 className="truncate text-lg font-bold text-gray-900 dark:text-gray-100 mb-1 group-hover:text-blue-600 dark:group-hover:text-blue-400 transition-colors">
              {song.title}
            </h3>
            <p className="truncate text-sm text-gray-600 dark:text-gray-400 font-medium">{artistLabel}</p>
          </div>

          <div className="flex items-center gap-3">
            <button
              type="button"
              onClick={togglePlay}
              className="p-3 flex items-center justify-center bg-blue-600 hover:bg-blue-700 rounded-full text-white transition-all hover:scale-105 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:scale-100"
              disabled={!hasAudio}
              aria-label={isPlaying ? "Pause" : "Play"}
            >
              {isPlaying ? <Pause className="h-5 w-5" /> : <Play className="h-5 w-5 ml-0.5" />}
            </button>

            {onDelete && (
              <button
                type="button"
                onClick={handleDelete}
                disabled={isDeleting}
                className="p-3 flex items-center justify-center rounded-full border border-gray-300 dark:border-gray-600 bg-white text-red-500 dark:bg-gray-800 transition-all hover:border-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 hover:text-red-600 dark:hover:text-red-400 disabled:opacity-50 disabled:cursor-not-allowed"
                aria-label="Delete song"
                title="Delete song"
              >
                {isDeleting ? (
                  <div className="h-5 w-5 animate-spin rounded-full border-2 border-gray-300 dark:border-gray-600 border-t-gray-700 dark:border-t-gray-300" />
                ) : (
                  <Trash2 className="h-5 w-5" />
                )}
              </button>
            )}

            <div
              className="
    relative
    h-12 w-12
    rounded-full
    bg-blue-600 hover:bg-blue-700
    text-white
    transition-all hover:scale-105
    flex items-center justify-center
  "
              onClick={(e) => e.stopPropagation()}
            >
              {/* Icon */}
              <ListMusic className="h-5 w-5 pointer-events-none" />

              {/* Invisible select */}
              <select
                value={selectedPlaylist}
                onChange={handleAddToPlaylist}
                className="
                absolute inset-0
                opacity-0
                cursor-pointer
                bg-white
                min-w-36 
              "
              >
                <option className="text-gray-500 hover:text-gray-500">--Select a playlist--</option>
                {playlists.map((pl) => (
                  <option key={pl.id} value={pl.id} className="text-black">
                    {pl.name}
                  </option>
                ))}
              </select>
            </div>

          </div>
        </div>

        {hasAudio ? (
          <div className="mt-4 pt-4 border-t border-gray-200 dark:border-gray-700">
            <audio
              ref={audioRef}
              src={song.audioUrl}
              preload="metadata"
              onPlay={() => setIsPlaying(true)}
              onPause={() => setIsPlaying(false)}
              onEnded={() => setIsPlaying(false)}
              onLoadedMetadata={(e) => setDuration(e.currentTarget.duration || 0)}
              onTimeUpdate={(e) => setCurrentTime(e.currentTarget.currentTime || 0)}
            />

            <div className="flex items-center gap-3">
              <span className="w-11 text-xs font-mono text-gray-600 dark:text-gray-400 tabular-nums">
                {formatTime(currentTime)}
              </span>

              <div className="relative flex-1 h-2 bg-gray-200 dark:bg-gray-700 rounded-full overflow-hidden group/progress cursor-pointer">
                <div
                  className="absolute inset-y-0 left-0 bg-linear-to-r from-blue-500 to-purple-500 rounded-full transition-all duration-150"
                  style={{
                    width: `${duration > 0 ? (currentTime / duration) * 100 : 0}%`,
                  }}
                />
                <input
                  className="absolute inset-0 w-full opacity-0 cursor-pointer"
                  type="range"
                  min={0}
                  max={duration || 0}
                  step={0.1}
                  value={Math.min(currentTime, duration || 0)}
                  onChange={(e) => {
                    const el = audioRef.current
                    if (!el) return
                    el.currentTime = Number(e.target.value)
                  }}
                />
              </div>

              <span className="w-11 text-xs font-mono text-gray-600 dark:text-gray-400 tabular-nums">
                {formatTime(duration)}
              </span>
            </div>
          </div>
        ) : (
          <p className="mt-4 pt-4 border-t border-gray-200 dark:border-gray-700 text-sm text-gray-500 dark:text-gray-400 italic">
            No audio attached.
          </p>
        )}
      </div>
    </li>
  )
}
