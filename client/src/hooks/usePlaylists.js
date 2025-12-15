import { useEffect, useState } from "react";
import { api } from "../constants/api-client";

export default function usePlaylists({ search, offset = 0, limit = 20 } = {}) {
    const [playlists, setPlaylists] = useState([]);
    const [error, setError] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [refetchFlag, setRefetchFlag] = useState(false);

    useEffect(() => {
        let isMounted = true;
        const controller = new AbortController();

        const fetchPlaylists = async () => {
            try {
                setIsLoading(true);
                setError("");

                const params = new URLSearchParams();

                if (search && search.trim().length > 0) {
                    params.set("search", search.trim());
                }

                params.set("offset", String(offset));
                params.set("limit", String(limit));

                const response = await api.get(
                    `/playlists?${params.toString()}`,
                    { signal: controller.signal }
                );

                if (!isMounted) return;

                // axios response shape
                setPlaylists(response.playlists ?? []);
            } catch (err) {
                if (!isMounted || controller.signal.aborted) return;

                setPlaylists([]);
                setError("Failed to load playlists");
                console.error("Fetch playlists error:", err);
            } finally {
                if (isMounted) {
                    setIsLoading(false);
                }
            }
        };

        fetchPlaylists();

        return () => {
            isMounted = false;
            controller.abort();
        };
    }, [search, offset, limit, refetchFlag]);

    return {
        playlists,
        isLoading,
        error,
        refetch: () => setRefetchFlag((flag) => !flag),
    };
}