import React from "react";
import AddArtistModal from "../components/add-artist-modal";
import { useRoutingContext } from "../hooks/useRoutingContext";
import { AddArtistModalContext } from "./AddArtistModalContext";

export function AddArtistModalProvider({ children }) {
  const { currentRoute } = useRoutingContext();
  const [isOpen, setIsOpen] = React.useState(false);
  const [events, setEvents] = React.useState([]);

  const openAddArtistModal = React.useCallback(() => {
    setIsOpen(true);
  }, []);

  const closeAddArtistModal = React.useCallback(() => {
    setIsOpen(false);
  }, []);

  const registerEvent = React.useCallback((event) => {
    setEvents((prev) => [...prev, event]);
  }, []);

  const fireAllEvents = React.useCallback(() => {
    events.forEach((event) => event());
    setEvents([]);
  }, [events]);

  React.useEffect(() => {
    if (currentRoute !== "artist" && isOpen) {
      setIsOpen(false);
    }
  }, [currentRoute, isOpen]);

  return (
    <AddArtistModalContext.Provider
      value={{
        isAddArtistModalOpen: isOpen,
        openAddArtistModal,
        closeAddArtistModal,
        registerEvent,
        fireAllEvents,
      }}
    >
      {children}
      <AddArtistModal open={isOpen} onClose={closeAddArtistModal} />
    </AddArtistModalContext.Provider>
  );
}
