import { PlaylistItem } from "@/components/playlist-item";
import { Music, RefreshCw } from "lucide-react";
import AddPlaylistButton from "../components/add-playlist-button";
import usePlaylist from "../hooks/usePlaylist";
import usePlaylists from "../hooks/usePlaylists";
import SubLayout from "../layouts/sublayout";
export default function PlayList() {
  const { playlists, isLoading, error, refetch } = usePlaylists({
    limit: 25,
    offset: 0,
  });

  const { deletePlaylist } = usePlaylist();

  if (error) {
    return (
      <div className="container mx-auto px-4 py-16">
        <div className="max-w-2xl mx-auto text-center">
          <h2 className="text-2xl font-bold text-red-600 dark:text-red-400 mb-2">
            Error Loading Playlists
          </h2>
          <p className="text-gray-600 dark:text-gray-400 mb-6">
            {error.message || "Something went wrong"}
          </p>
          <button
            onClick={refetch}
            className="inline-flex items-center px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors"
          >
            <RefreshCw className="w-4 h-4 mr-2" />
            Try Again
          </button>
        </div>
      </div>
    );
  }

  return (
    <SubLayout>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-4xl font-bold text-white mb-2">Playlists</h1>
          <p className="text-gray-600 dark:text-gray-600">
            Manage and explore your music collections
          </p>
        </div>
        <AddPlaylistButton onAdd={refetch} />
      </div>

      {isLoading && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[...Array(6)].map((_, i) => (
            <div
              key={i}
              className="h-48 bg-gray-200 dark:bg-gray-700 animate-pulse rounded-lg"
            />
          ))}
        </div>
      )}

      {!isLoading && playlists && playlists.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {playlists.map((playlist) => (
            <PlaylistItem
              key={playlist.id}
              playlist={playlist}
              onDelete={() => {
                if (
                  confirm("Do you want to delete this playlist") &&
                  deletePlaylist(playlist.id)
                ) {
                  refetch();
                }
              }}
            />
          ))}
        </div>
      )}

      {!isLoading && (!playlists || playlists.length === 0) && (
        <div className="text-center py-16">
          <div className="w-16 h-16 bg-gray-200 dark:bg-gray-700 rounded-full flex items-center justify-center mx-auto mb-4">
            <Music className="w-8 h-8 text-gray-500 dark:text-gray-400" />
          </div>
          <h2 className="text-2xl font-semibold text-white dark:text-gray-100 mb-2">
            No Playlists Yet
          </h2>
          <p className="text-gray-600 dark:text-gray-400 mb-6">
            Create your first playlist to get started
          </p>
          <AddPlaylistButton
            onAdd={refetch}
            className={
              "inline-flex items-center px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white font-medium rounded-lg transition-colors"
            }
          />
        </div>
      )}
    </SubLayout>
  );
}
