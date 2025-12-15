import React from "react";
import AddPlaylistModal from '../components/add-playlist-modal';
import { useRoutingContext } from "../hooks/useRoutingContext";
import { AddPlaylistModalContext } from './AddPlaylistModalContext';

export function AddPlaylistModalProvider({ children }) {
    const { currentRoute } = useRoutingContext();
    const [isOpen, setIsOpen] = React.useState(false);
    const onSuccessRef = React.useRef(null);

    const openAddPlaylistModal = React.useCallback(() => {
        setIsOpen(true);
    }, []);

    const closeAddPlaylistModal = React.useCallback(() => {
        setIsOpen(false);
    }, []);


    const fireOnSuccess = () => {
        onSuccessRef.current?.();
    };

    const setCallback = (fn) => {
        onSuccessRef.current = fn;
    }

    React.useEffect(() => {
        if (currentRoute !== "playlist" && isOpen) {
            setIsOpen(false);
        }
    }, [currentRoute, isOpen]);

    return (
        <AddPlaylistModalContext.Provider
            value={{
                isAddPlaylistModalOpen: isOpen,
                openAddPlaylistModal,
                closeAddPlaylistModal,
                fireOnSuccess,
                setCallback,
            }}
        >
            {children}
            <AddPlaylistModal
                open={isOpen}
                onClose={closeAddPlaylistModal}
            />
        </AddPlaylistModalContext.Provider>
    );
}

