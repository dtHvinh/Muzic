import {RefreshCw} from "lucide-react";

export default function PlaylistItemError({message, onReload}) {
    return (
        <div className="container mx-auto px-4 py-16">
            <div className="max-w-2xl mx-auto text-center">
                <h2 className="text-2xl font-bold text-red-600 dark:text-red-400 mb-2">
                    Error Loading Playlists
                </h2>
                <p className="text-gray-600 dark:text-gray-400 mb-6">
                    {message || "Something went wrong"}
                </p>
                <button
                    onClick={onReload}
                    className="inline-flex items-center px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white
                    font-medium rounded-lg transition-colors"
                >
                    <RefreshCw className="w-4 h-4 mr-2"/>
                    Try Again
                </button>
            </div>
        </div>
    )
}