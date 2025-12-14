import { Plus } from "lucide-react";
import { useAddArtistModal } from "../hooks/useAddArtistModal";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function AddArtistButton() {
  const { currentRoute } = useRoutingContext();
  const { openAddArtistModal } = useAddArtistModal();

  if (currentRoute !== "artist") return null;

  return (
    <button
      className="h-9 flex items-center justify-between text-primary bg-transparent!"
      onClick={openAddArtistModal}
      aria-label="Add artist"
    >
      <Plus /> Add
    </button>
  );
}
