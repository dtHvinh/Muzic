import { useContext } from "react";
import { AddSongModalContext } from "../context/AddSongModalContext";

export function useAddSongModal() {
  const ctx = useContext(AddSongModalContext);
  if (!ctx) {
    throw new Error(
      "useAddSongModal must be used within <AddSongModalProvider>"
    );
  }
  return ctx;
}
