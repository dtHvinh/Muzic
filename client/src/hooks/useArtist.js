import { useState } from "react";
import { toast } from 'sonner';
import { api } from '../constants/api-client';

export function useArtist() {
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);
    const [error, setError] = useState(null);
    const [isSuccess, setIsSuccess] = useState(false);

    const createArtist = async (inputData) => {
        setIsLoading(true);
        setIsError(false);
        setError(null);
        setIsSuccess(false);

        try {
            const newArtist = await api.post('/artists', inputData);

            setIsSuccess(true);
            toast.success('Artist created successfully!');

            return newArtist
        } catch (err) {
            const message = err.response?.data?.message || err.message || 'Failed to create artist';
            setError(message);
            setIsError(true);
            console.error('Create artist error:', err);
            return null;
        } finally {
            setIsLoading(false);
        }
    };

    const deleteArtist = async (artistId) => {
        if (!artistId) {
            toast.error('Invalid artist ID');
            return false;
        }

        setIsLoading(true);

        try {
            await api.del(`/artists/?id=${artistId}`);
            setIsSuccess(true);
            toast.success('Artist deleted successfully');
            return true;
        } catch (err) {
            const message = err.response?.data?.message || err.message || 'Failed to delete artist';
            setError(message);
            setIsError(true);
            toast.error(message);
            console.error('Delete artist error:', err);
            return false;
        } finally {
            setIsLoading(false);
        }
    };

    return {
        createArtist,
        deleteArtist,
        isLoading,
        isError,
        error,
        isSuccess,
    };
}