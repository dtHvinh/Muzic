import React from "react";
import { AddArtistModalContext } from "../context/AddArtistModalContext";

export function useAddArtistModal() {
  const ctx = React.useContext(AddArtistModalContext);
  if (!ctx) {
    throw new Error(
      "useAddArtistModal must be used inside <AddArtistModalProvider>"
    );
  }
  return ctx;
}
