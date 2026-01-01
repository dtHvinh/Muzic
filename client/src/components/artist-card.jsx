import { Edit2, Trash2 } from "lucide-react";
import { useState } from "react";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function ArtistCard({
    artist,
    cardProps = { size: 28 },
    onDelete,
}) {
    const { currentRoute, navigateTo } = useRoutingContext();
    const [isDeleting, setIsDeleting] = useState(false);

    const handleDelete = async (e) => {
        e.stopPropagation(); // prevent card click
        if (!onDelete || isDeleting) return;

        setIsDeleting(true);
        try {
            await onDelete(artist.id);
        } catch (err) {
            console.error("Delete failed:", err);
        } finally {
            setIsDeleting(false);
        }
    };

    return (
        <div className="group relative cursor-pointer text-center">
            {(currentRoute === "artist" || currentRoute === "search") && (
                <div
                    className="absolute right-2 top-2 z-10 flex flex-col gap-2 opacity-0 transition-all duration-300 group-hover:opacity-100">
                    <button
                        onClick={(e) => {
                            e.stopPropagation();
                            navigateTo("artist/edit", { id: artist.id });
                        }}
                        className="rounded-full bg-blue-500/90 p-2 text-white backdrop-blur-sm hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-blue-400"
                        title="Edit artist"
                    >
                        <Edit2 className="h-4 w-4" />
                    </button>

                    <button
                        onClick={handleDelete}
                        disabled={isDeleting}
                        className="rounded-full bg-red-500/90 p-2 text-white backdrop-blur-sm hover:bg-red-600 focus:outline-none focus:ring-2 focus:ring-red-400 disabled:opacity-50"
                        title="Delete artist"
                    >
                        {isDeleting ? (
                            <div className="h-4 w-4 animate-spin rounded-full border-2 border-white/30 border-t-white" />
                        ) : (
                            <Trash2 className="h-4 w-4" />
                        )}
                    </button>
                </div>
            )}

            <div
                className="pointer-events-none absolute inset-0 rounded-full bg-black/10 opacity-0 transition-opacity group-hover:opacity-100" />

            <div
                className={`relative mx-auto mb-3 h-${cardProps.size} w-${cardProps.size} overflow-hidden rounded-full ring-4 ring-transparent transition-all group-hover:ring-primary/20`}
            >
                <img
                    src={`https://api.dicebear.com/7.x/avataaars/svg?seed=${artist.name}`}
                    alt={artist.name}
                    className="aspect-square w-full object-cover transition-transform duration-300 group-hover:scale-110"
                />
            </div>

            <h3 className="truncate font-medium text-foreground transition-colors group-hover:text-primary">
                {artist.name}
            </h3>
            <p className="text-xs text-muted-foreground">
                {artist.genre || "Artist"}
            </p>
        </div>
    );
}

export function SkeletonArtistCard({ cardProps = { size: 24 } }) {
    return (
        <div className="group cursor-pointer text-center animate-pulse">
            <div
                className={`
                relative mx-auto mb-3 
                h-${cardProps.size} w-${cardProps.size} 
                overflow-hidden rounded-full 
                bg-gray-200 dark:bg-gray-700
                `}
            >
                <img
                    src={`https://placehold.co/${cardProps.size}x${cardProps.size}?text=Wait`}
                    className="aspect-square w-full object-cover transition-all group-hover:scale-105"
                />
            </div>
            <h3 className="mb-1 h-5 w-3/4 mx-auto rounded bg-gray-200 dark:bg-gray-700" />
            <p className="h-4 w-1/2 mx-auto rounded bg-gray-200 dark:bg-gray-700" />
        </div>
    );
}
