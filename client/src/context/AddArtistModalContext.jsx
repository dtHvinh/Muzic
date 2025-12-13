import React from "react";
import AddArtistModal from "../components/add-artist-modal";
import { useRoutingContext } from "./RoutingContext";

const AddArtistModalContext = React.createContext(null);

export function AddArtistModalProvider({ children }) {
  const { currentRoute } = useRoutingContext();
  const [isOpen, setIsOpen] = React.useState(false);

  const openAddArtistModal = React.useCallback(() => {
    setIsOpen(true);
  }, []);

  const closeAddArtistModal = React.useCallback(() => {
    setIsOpen(false);
  }, []);

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
      }}
    >
      {children}
      <AddArtistModal open={isOpen} onClose={closeAddArtistModal} />
    </AddArtistModalContext.Provider>
  );
}

export function useAddArtistModal() {
  const ctx = React.useContext(AddArtistModalContext);
  if (!ctx) {
    throw new Error(
      "useAddArtistModal must be used inside <AddArtistModalProvider>"
    );
  }
  return ctx;
}
