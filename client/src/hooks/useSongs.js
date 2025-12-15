import { useEffect, useState } from "react";
import { api } from "../constants/api-client";

export function useSongs({ limit = 20, offset = 0 }) {
  const [songs, setSongs] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [flag, setFlag] = useState(false);

  useEffect(() => {
    const fetchSongs = async () => {
      setIsLoading(true);
      try {
        const data = await api.get(`/songs?limit=${limit}&offset=${offset}`);
        setSongs(data.songs ?? []);
      } finally {
        setIsLoading(false);
      }
    };
    fetchSongs();
  }, [flag, limit, offset]);

  return { songs, isLoading, refetch: () => setFlag((f) => !f) };
}
