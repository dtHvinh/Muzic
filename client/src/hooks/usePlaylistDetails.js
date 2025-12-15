import { useEffect, useState } from "react";
import { api } from "../constants/api-client";

export default function usePlaylistDetails(playlistId) {
  const [playlist, setPlaylist] = useState(null);
  const [songs, setSongs] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const [refetchFlag, setRefetchFlag] = useState(false);

  useEffect(() => {
    if (playlistId == null || String(playlistId).trim() === "") {
      setPlaylist(null);
      setSongs([]);
      setError("Missing playlist id");
      return;
    }

    let isMounted = true;
    const controller = new AbortController();

    const fetchDetails = async () => {
      try {
        setIsLoading(true);
        setError("");

        const [playlistRes, songsRes] = await Promise.all([
          api.get(`/playlists?id=${encodeURIComponent(String(playlistId))}`, {
            signal: controller.signal,
          }),
          api.get(
            `/playlists?mode=details&id=${encodeURIComponent(
              String(playlistId)
            )}`,
            { signal: controller.signal }
          ),
        ]);

        if (!isMounted) return;

        setPlaylist(playlistRes ?? null);
        setSongs(songsRes?.songs ?? []);
      } catch (err) {
        if (!isMounted || controller.signal.aborted) return;

        setPlaylist(null);
        setSongs([]);
        setError("Failed to load playlist details");
        console.error("Fetch playlist details error:", err);
      } finally {
        if (isMounted) setIsLoading(false);
      }
    };

    fetchDetails();

    return () => {
      isMounted = false;
      controller.abort();
    };
  }, [playlistId, refetchFlag]);

  return {
    playlist,
    songs,
    isLoading,
    error,
    refetch: () => setRefetchFlag((f) => !f),
  };
}
