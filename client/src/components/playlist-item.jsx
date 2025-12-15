import dayjs from "dayjs";
import { Music, Trash2 } from "lucide-react";
import { useRoutingContext } from '../hooks/useRoutingContext';

export function PlaylistItem({ playlist, onDelete }) {
    const { navigateTo } = useRoutingContext();

    const handleNavigate = () => {
        navigateTo("playlist/details", { id: playlist.id });
    };

    return (
        <div
            onClick={handleNavigate}
            className="
        relative flex flex-col bg-white dark:bg-gray-800
        rounded-lg border border-gray-200 dark:border-gray-700
        p-6 cursor-pointer group overflow-hidden
        hover:shadow-lg transition-shadow
      "
        >
            <div className="pointer-events-none absolute inset-0 bg-black/20 opacity-0 group-hover:opacity-100 transition-opacity" />

            <button
                onClick={(e) => {
                    e.stopPropagation();
                    onDelete?.(playlist);
                }}
                className="
          absolute bottom-4 right-4 z-10
          opacity-0 group-hover:opacity-100
          transition-all duration-200
          scale-95 group-hover:scale-100
          rounded-full bg-red-600 p-3
          text-white shadow-lg
          hover:bg-red-700
        "
                aria-label="Delete playlist"
            >
                <Trash2 className="w-4 h-4" />
            </button>

            {/* Content */}
            <div className="relative z-0 flex items-start justify-between gap-4 mb-4">
                <div className="flex-1 min-w-0">
                    <h3 className="text-xl font-semibold text-gray-900 dark:text-gray-100 line-clamp-1">
                        {playlist.name}
                    </h3>
                    <p className="mt-1.5 text-sm text-gray-600 dark:text-gray-400">
                        {playlist.description}
                    </p>
                </div>

                <div className="shrink-0 w-12 h-12 rounded-lg bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center">
                    <Music className="w-6 h-6 text-blue-600 dark:text-blue-400" />
                </div>
            </div>

            <p className="relative z-0 text-sm text-gray-500 dark:text-gray-400 mt-auto">
                Created {dayjs(playlist.createdAt).fromNow()}
            </p>
        </div>
    );
}
