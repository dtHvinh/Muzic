import { Plus } from "lucide-react";
import { useEffect } from "react";
import { useAddSongModal } from "../hooks/useAddSongModal";
import { useRoutingContext } from "../hooks/useRoutingContext";

export default function AddSongButton({ onAdd }) {
  const { currentRoute } = useRoutingContext();
  const { openAddSongModal, setCallback } = useAddSongModal();

  useEffect(() => {
    setCallback(onAdd);
  }, []);

  if (currentRoute !== "song") return null;

  return (
    <button
      className="flex h-9 items-center gap-2"
      onClick={openAddSongModal}
    >
      <Plus /> New Song
    </button>
  );
}
