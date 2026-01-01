import {useState} from "react";
import {toast} from "sonner";
import {api} from "../constants/api-client";

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

    const updatePlaylist = async (id, inputData) => {
        setIsLoading(true);
        setIsError(false);
        setError(null);
        setIsSuccess(false);

        try {
            const response = await api.put(`/playlists?id=${id}`, inputData);

            setIsSuccess(true);
            toast.success("Playlist updated successfully!");

            return response;
        } catch (err) {
            const message =
                err.response?.data?.message ||
                err.message ||
                "Failed to update playlist";

            setError(message);
            setIsError(true);
            toast.error(message);
            console.error("Update playlist error:", err);

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

    const addPlaylistItem = async (playlistId, songId) => {
        if (!playlistId || !songId) {
            toast.error("Invalid playlist ID or song ID");
            return false;
        }
        setIsLoading(true);
        setIsError(false);
        setError(null);

        try {
            await api.post(
                `/playlists?mode=add-song&playlistId=${playlistId}&songId=${songId}`
            );
            setIsSuccess(true);
            toast.success("Song added to playlist successfully");
            return true;
        } catch (err) {
            const message =
                err.response?.data?.message ||
                err.message ||
                "Failed to add song to playlist";
            setError(message);
            setIsError(true);
            toast.error(message);
            console.error("Add playlist item error:", err);
            return false;
        } finally {
            setIsLoading(false);
        }
    };

    const removePlaylistItem = async (playlistId, songId) => {
        if (!playlistId || !songId) {
            toast.error("Invalid playlist ID or song ID");
            return false;
        }
        setIsLoading(true);
        setIsError(false);
        setError(null);

        try {
            await api.post(
                `/playlists?mode=remove-song&playlistId=${playlistId}&songId=${songId}`
            );
            setIsSuccess(true);
            toast.success("Song removed from playlist successfully");
            return true;
        } catch (err) {
            const message =
                err.response?.data?.message ||
                err.message ||
                "Failed to remove song from playlist";
            setError(message);
            setIsError(true);
            toast.error(message);
            console.error("Remove playlist item error:", err);
            return false;
        } finally {
            setIsLoading(false);
        }
    };

    return {
        createPlaylist,
        deletePlaylist,
        updatePlaylist,
        addPlaylistItem,
        removePlaylistItem,
        isLoading,
        isError,
        error,
        isSuccess,
    };
}
