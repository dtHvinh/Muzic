import { Plus } from "lucide-react";
import { useEffect } from "react";
import { useAddPlaylistModal } from "../hooks/useAddPlaylistModal";
import { useRoutingContext } from "../hooks/useRoutingContext";
import { cn } from '../lib/utils';

export default function AddPlaylistButton({ className, onAdd }) {
    const { currentRoute } = useRoutingContext();
    const { openAddPlaylistModal, setCallback } = useAddPlaylistModal();

    useEffect(() => {
        setCallback(onAdd)
    }, [])

    if (currentRoute !== "playlist") return null;

    return (
        <button
            className={cn("h-9 flex items-center gap-2 justify-between bg-transparent!", className)}
            onClick={openAddPlaylistModal}
            aria-label="Add artist"
        >
            <Plus /> Add Playlist
        </button>
    );
}
