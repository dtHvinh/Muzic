import { useState } from "react";
import { toast } from 'sonner';
import { api } from '../constants/api-client';

export default function usePlaylist() {
    const [isLoading, setIsLoading] = useState(false);
    const [isError, setIsError] = useState(false);
    const [error, setError] = useState(null);
    const [isSuccess, setIsSuccess] = useState(false);

    const createPlaylist = async (inputData) => {
        setIsLoading(true);
        setIsError(false);
        setError(null);
        setIsSuccess(false);

        try {
            const response = await api.post("/playlists", inputData);

            setIsSuccess(true);
            toast.success("Playlist created successfully!");

            return response;
        } catch (err) {
            const message =
                err.response?.data?.message ||
                err.message ||
                "Failed to create playlist";

            setError(message);
            setIsError(true);
            toast.error(message);
            console.error("Create playlist error:", err);

            return null;
        } finally {
            setIsLoading(false);
        }
    };

    const deletePlaylist = async (playlistId) => {
        if (!playlistId) {
            toast.error("Invalid playlist ID");
            return false;
        }

        setIsLoading(true);
        setIsError(false);
        setError(null);

        try {
            await api.del(`/playlists?id=${playlistId}`);

            setIsSuccess(true);
            toast.success("Playlist deleted successfully");

            return true;
        } catch (err) {
            const message =
                err.response?.data?.message ||
                err.message ||
                "Failed to delete playlist";

            setError(message);
            setIsError(true);
            toast.error(message);
            console.error("Delete playlist error:", err);

            return false;
        } finally {
            setIsLoading(false);
        }
    };

    return {
        createPlaylist,
        deletePlaylist,
        isLoading,
        isError,
        error,
        isSuccess,
    };
}