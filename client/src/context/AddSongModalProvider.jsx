import React from "react";
import { AddSongModalContext } from "./AddSongModalContext";
import AddSongModal from "../components/add-song-modal";
import { useRoutingContext } from "../hooks/useRoutingContext";

export function AddSongModalProvider({ children }) {
  const { currentRoute } = useRoutingContext();
  const [open, setOpen] = React.useState(false);
  const [events, setEvents] = React.useState([]);

  const registerEvent = React.useCallback((cb) => {
    setEvents((prev) => [...prev, cb]);
  }, []);

  const fireAllEvents = React.useCallback(() => {
    events.forEach((cb) => cb());
    setEvents([]);
  }, [events]);

  React.useEffect(() => {
    if (currentRoute !== "song" && open) setOpen(false);
  }, [currentRoute, open]);

  return (
    <AddSongModalContext.Provider
      value={{
        isAddSongModalOpen: open,
        openAddSongModal: () => setOpen(true),
        closeAddSongModal: () => setOpen(false),
        registerEvent,
        fireAllEvents,
      }}
    >
      {children}
      <AddSongModal open={open} onClose={() => setOpen(false)} />
    </AddSongModalContext.Provider>
  );
}
