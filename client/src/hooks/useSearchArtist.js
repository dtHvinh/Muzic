import { useEffect, useState } from "react";
import { api } from "../constants/api-client";

/**
 * @returns {{ artists: Artist[], isLoading: boolean }}
 */
export const useSearchArtist = ({ query = "a", offset = 0, limit = 20 }) => {
  const [artists, setArtists] = useState([]);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [refetchFlag, setRefetchFlag] = useState(false);

  useEffect(() => {
    let isMounted = true;
    const controller = new AbortController();

    const fetch = async () => {
      try {
        setIsLoading(true);
        setError("");
        const normalizedQuery = query?.trim()?.length ? query.trim() : "a";
        const params = new URLSearchParams({
          query: normalizedQuery,
          offset: String(offset ?? 0),
          limit: String(limit ?? 20),
        });
        const response = await api.get(`/artists?${params.toString()}`, {
          signal: controller.signal,
        });
        if (!isMounted) return;
        setArtists(response.artists ?? []);
      } catch (err) {
        if (!isMounted || controller.signal.aborted) return;
        setArtists([]);
        setError("Failed to load artist");
        console.error("Search artist error:", err);
      } finally {
        if (isMounted) {
          setIsLoading(false);
        }
      }
    };

    fetch();

    return () => {
      isMounted = false;
      controller.abort();
    };
  }, [query, offset, limit, refetchFlag]);

  return {
    artists,
    isLoading,
    error,
    reset: () => setRefetchFlag((flag) => !flag),
  };
};
