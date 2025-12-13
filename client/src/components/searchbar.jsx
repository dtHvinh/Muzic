import { Search } from 'lucide-react';
import { useRoutingContext } from '../context/RoutingContext';
import AddArtistButton from './add-artist-button';

export default function SearchBar() {
    const { currentRoute } = useRoutingContext();
    return (
        <header className="sticky top-0 z-10 grid grid-cols-12 items-center gap-4 bg-background/80 px-12 py-4 backdrop-blur-md">
            <div className="col-span-4 flex justify-start">
            </div>

            <div className="col-span-4 relative">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <input
                    type="text"
                    placeholder="Search for songs, artists, playlists..."
                    className="h-10 w-full rounded-full bg-secondary pl-10 pr-4 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
                />
            </div>

            <div className="col-span-4 flex justify-end">
                <AddArtistButton />
            </div>
        </header>
    )
}