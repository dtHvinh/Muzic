import React from "react";
import { AddPlaylistModalContext } from "../context/AddPlaylistModalContext";

export function useAddPlaylistModal() {
    const ctx = React.useContext(AddPlaylistModalContext);
    if (!ctx) {
        throw new Error(
            "useAddPlaylistModal must be used inside <AddPlaylistModalProvider>"
        );
    }
    return ctx;
}
