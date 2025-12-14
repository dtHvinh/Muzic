import { Plus } from "lucide-react";
import { useEffect } from "react";
import { useAddArtistModal } from "../hooks/useAddArtistModal";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function AddArtistButton({ onAdd }) {
  const { currentRoute } = useRoutingContext();
  const { openAddArtistModal, registerEvent } = useAddArtistModal();

  useEffect(() => {
    if (onAdd) {
      const event = () => {
        onAdd();
      };
      registerEvent(event);
    }
  }, [onAdd, registerEvent]);

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
