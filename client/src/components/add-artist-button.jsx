import { Plus } from "lucide-react";
import { useAddArtistModal } from "../hooks/useAddArtistModal";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function AddArtistButton({ onAdd }) {
  const { currentRoute } = useRoutingContext();
  const { openAddArtistModal, setCallback } = useAddArtistModal();

  if (currentRoute !== "artist") return null;

  return (
    <button
      className="h-9 flex items-center gap-2 justify-between bg-transparent!"
      onClick={() => {
        openAddArtistModal();
        setCallback(onAdd);
      }}
      aria-label="Add artist"
    >
      <Plus /> Add Artist
    </button>
  );
}
