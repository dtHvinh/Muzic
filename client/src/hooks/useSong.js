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
      const message = err.response?.data?.reason || "Failed to create song";
      toast.error(message);
      return null;
    } finally {
      setIsLoading(false);
    }
  };

  return { createSong, isLoading };
}
