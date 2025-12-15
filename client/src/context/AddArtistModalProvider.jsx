import React from "react";
import AddArtistModal from "../components/add-artist-modal";
import { useRoutingContext } from "../hooks/useRoutingContext";
import { AddArtistModalContext } from "./AddArtistModalContext";

export function AddArtistModalProvider({ children }) {
  const { currentRoute } = useRoutingContext();
  const [isOpen, setIsOpen] = React.useState(false);
  const onSuccessRef = React.useRef(null);

  const openAddArtistModal = React.useCallback(() => {
    setIsOpen(true);
  }, []);

  const closeAddArtistModal = React.useCallback(() => {
    setIsOpen(false);
  }, []);

  const fireOnSuccess = () => {
    onSuccessRef.current?.();
  };

  const setCallback = (fn) => {
    onSuccessRef.current = fn;
  }

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
        fireOnSuccess,
        setCallback
      }}
    >
      {children}
      <AddArtistModal open={isOpen} onClose={closeAddArtistModal} />
    </AddArtistModalContext.Provider>
  );
}
