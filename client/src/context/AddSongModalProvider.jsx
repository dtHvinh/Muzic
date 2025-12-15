import React from "react";
import AddSongModal from "../components/add-song-modal";
import { useRoutingContext } from "../hooks/useRoutingContext";
import { AddSongModalContext } from "./AddSongModalContext";

export function AddSongModalProvider({ children }) {
  const { currentRoute } = useRoutingContext();
  const [open, setOpen] = React.useState(false);
  const onSuccessRef = React.useRef(null);

  const fireOnSuccess = () => {
    onSuccessRef.current?.();
  };

  const setCallback = (fn) => {
    onSuccessRef.current = fn;
  }

  React.useEffect(() => {
    if (currentRoute !== "song" && open) setOpen(false);
  }, [currentRoute, open]);

  return (
    <AddSongModalContext.Provider
      value={{
        isAddSongModalOpen: open,
        openAddSongModal: () => setOpen(true),
        closeAddSongModal: () => setOpen(false),
        fireOnSuccess,
        setCallback,
      }}
    >
      {children}
      <AddSongModal open={open} onClose={() => setOpen(false)} />
    </AddSongModalContext.Provider>
  );
}
