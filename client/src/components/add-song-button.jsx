import { Plus } from "lucide-react";
import { useEffect } from "react";
import { useAddSongModal } from "../hooks/useAddSongModal";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function AddSongButton({ onAdd }) {
  const { currentRoute } = useRoutingContext();
  const { openAddSongModal, registerEvent } = useAddSongModal();

  useEffect(() => {
    if (!onAdd) return;
    const event = () => onAdd();
    registerEvent(event);
  }, [onAdd, registerEvent]);

  if (currentRoute !== "song") return null;

  return (
    <button
      className="flex h-9 items-center gap-2 text-primary bg-transparent!"
      onClick={openAddSongModal}
    >
      <Plus /> Add Song
    </button>
  );
}
