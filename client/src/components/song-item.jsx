"use client";

import { Music, Pause, Play } from "lucide-react";
import React from "react";

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
}) {
  const audioRef = React.useRef(null);

  const [isPlaying, setIsPlaying] = React.useState(false);
  const [duration, setDuration] = React.useState(0);
  const [currentTime, setCurrentTime] = React.useState(0);

  React.useEffect(() => {
    if (!isActive && audioRef.current) {
      audioRef.current.pause();
    }
  }, [isActive]);

  const hasAudio = Boolean(song?.audioUrl);
  const artistLabel =
    song?.artistName ||
    (song?.artistId != null ? `Artist #${song.artistId}` : "Unknown artist");

  const togglePlay = async () => {
    if (!hasAudio) return;

    const el = audioRef.current;
    if (!el) return;

    if (isPlaying) {
      onPauseRequest?.(el, song.id);
      return;
    }

    onPlayRequest?.(el, song.id);
  };

  return (
    <li className="group relative overflow-hidden rounded-2xl h-44 bg-linear-to-br from-secondary/40 to-secondary/20 backdrop-blur-sm border border-white/5 transition-all duration-300 hover:border-white/10 hover:shadow-lg">
      <div className="p-5">
        <div className="flex items-center gap-4">
          <div className="relative shrink-0">
            <div className="h-16 w-16 rounded-xl bg-linear-to-br from-primary/20 to-primary/5 flex items-center justify-center border border-white/10 shadow-md">
              <Music className="h-7 w-7 text-primary/60" />
            </div>
            {isPlaying && (
              <div className="absolute inset-0 rounded-xl bg-primary/10 animate-pulse" />
            )}
          </div>

          <div className="flex-1 min-w-0">
            <h3 className="truncate text-lg font-bold text-foreground mb-1 group-hover:text-primary transition-colors">
              {song.title}
            </h3>
            <p className="truncate text-sm text-muted-foreground font-medium">
              {artistLabel}
            </p>
          </div>

          <button
            type="button"
            onClick={togglePlay}
            disabled={!hasAudio}
            className="relative shrink-0 inline-flex h-12 w-12 items-center justify-center rounded-full bg-primary! text-primary-foreground shadow-lg transition-all duration-200 hover:scale-110 hover:shadow-xl disabled:cursor-not-allowed disabled:opacity-40 disabled:hover:scale-100"
            aria-label={isPlaying ? "Pause" : "Play"}
          >
            {isPlaying ? (
              <Pause className="h-12 w-12 fill-current" />
            ) : (
              <Play className="h-12 w-12 fill-current ml-0.5" />
            )}
          </button>
        </div>

        {hasAudio ? (
          <div className="mt-4 pt-4 border-t border-white/5">
            <audio
              ref={audioRef}
              src={song.audioUrl}
              preload="metadata"
              onPlay={() => setIsPlaying(true)}
              onPause={() => setIsPlaying(false)}
              onEnded={() => setIsPlaying(false)}
              onLoadedMetadata={(e) =>
                setDuration(e.currentTarget.duration || 0)
              }
              onTimeUpdate={(e) =>
                setCurrentTime(e.currentTarget.currentTime || 0)
              }
            />

            <div className="flex items-center gap-3">
              <span className="w-11 text-xs font-mono text-muted-foreground tabular-nums">
                {formatTime(currentTime)}
              </span>

              <div className="relative flex-1 h-2 bg-white/5 rounded-full overflow-hidden group/progress">
                <div
                  className="absolute inset-y-0 left-0 bg-linear-to-r from-primary to-primary/80 rounded-full transition-all duration-150"
                  style={{
                    width: `${
                      duration > 0 ? (currentTime / duration) * 100 : 0
                    }%`,
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
                    const el = audioRef.current;
                    if (!el) return;
                    el.currentTime = Number(e.target.value);
                  }}
                />
              </div>

              <span className="w-11 text-xs font-mono text-muted-foreground tabular-nums">
                {formatTime(duration)}
              </span>
            </div>
          </div>
        ) : (
          <p className="mt-4 pt-4 border-t border-white/5 text-sm text-muted-foreground italic">
            No audio attached.
          </p>
        )}
      </div>
    </li>
  );
}
