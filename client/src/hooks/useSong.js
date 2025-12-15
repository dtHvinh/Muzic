import { useState } from "react";
import { toast } from "sonner";
import { api } from "../constants/api-client";

export function useSong() {
  const [isLoading, setIsLoading] = useState(false);

  const createSong = async (payload) => {
    setIsLoading(true);
    try {
      const res = await api.post("/songs", payload);
      toast.success("Song created");
      return res;
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.response?.data?.reason ||
        "Failed to create song";
      toast.error(message);
      return null;
    } finally {
      setIsLoading(false);
    }
  };

  const deleteSong = async (songId) => {
    if (!songId) {
      toast.error("Invalid song ID");
      return false;
    }

    setIsLoading(true);
    try {
      await api.del(`/songs/?id=${songId}`);
      toast.success("Song deleted");
      return true;
    } catch (err) {
      const message =
        err.response?.data?.message ||
        err.response?.data?.reason ||
        "Failed to delete song";
      toast.error(message);
      return false;
    } finally {
      setIsLoading(false);
    }
  };

  return { createSong, deleteSong, isLoading };
}
