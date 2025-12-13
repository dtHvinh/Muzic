import { useEffect, useState } from "react";
import { api } from "../constants/api-client";


/**
 * @returns {{ artists: Artist[], isLoading: boolean }}
 */
export const useSearchArtist = ({
    query,
    offset,
    limit
}) => {
    const [artists, setArtists] = useState([])
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const [refetchFlag, setRefetchFlag] = useState(false);

    useEffect(() => {
        const fetch = async () => {
            try {
                setIsLoading(true);
                const response = await api.get(`/artists?query=${query ?? 'a'}&offset=${offset}&limit=${limit}`);
                setArtists(response.artists);
            }
            catch {
                setError('Failed to load artist')
            } finally {
                setIsLoading(false);
            }
        }

        fetch();
    }, [refetchFlag])

    return {
        artists,
        isLoading,
        error,
        reset: () => setRefetchFlag(!refetchFlag)
    }
}