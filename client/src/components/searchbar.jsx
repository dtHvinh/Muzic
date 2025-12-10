import { Search } from 'lucide-react';

export default function SearchBar() {
    return (
        <header className="sticky top-0 z-10 flex items-center justify-center bg-background/80 px-6 py-4 backdrop-blur-md">
            <div className="relative">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <input
                    type="text"
                    placeholder="Search for songs, artists, playlists..."
                    className="h-10 w-96 rounded-full bg-secondary pl-10 pr-4 text-sm text-foreground placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-primary"
                />
            </div>
        </header>
    )
}