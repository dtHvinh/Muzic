import {Search} from "lucide-react";
import {useRoutingContext} from "@/hooks/useRoutingContext.jsx";
import {useEffect, useState} from "react";

export default function SearchBar() {
    const {navigateTo, setQuery} = useRoutingContext();
    const [term, setTerm] = useState("");

    useEffect(() => {
        const id = setTimeout(() => {
            setQuery({term});
        }, 500);

        return () => clearTimeout(id);
    }, [term]);

    return (
        <header
            className="sticky top-0 z-10 grid grid-cols-12 items-center gap-4 bg-background/80 px-12 py-4 backdrop-blur-md">
            <div className="col-span-4 flex justify-start"></div>

            <div className="col-span-4 relative">
                <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground"/>
                <input
                    onChange={(e) => {
                        navigateTo('search')
                        setTerm(e.target.value)
                    }}
                    type="text"
                    placeholder="Search for songs, artists..."
                    className="h-10 w-full rounded-full bg-secondary pl-10 pr-4 text-sm focus:outline-none focus:ring-2 focus:ring-primary"
                />
            </div>

            <div className="col-span-4 flex justify-end"></div>
        </header>
    );
}
