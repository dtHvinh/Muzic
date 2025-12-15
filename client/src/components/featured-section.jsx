import { Play } from "lucide-react"

const featuredSongs = [
    {
        id: 1,
        title: "Midnight Dreams",
        artist: "Luna Nova",
        color: "from-purple-600/40",
    },
    {
        id: 2,
        title: "Electric Soul",
        artist: "The Voltage",
        color: "from-blue-600/40",
    },
    {
        id: 3,
        title: "Golden Hour",
        artist: "Sunset Collective",
        color: "from-orange-600/40",
    },
]

export default function FeaturedSection() {
    return (
        <section>
            <h2 className="mb-4 text-2xl font-bold text-foreground">Featured This Week</h2>
            <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3">
                {featuredSongs.map((song) => (
                    <div
                        key={song.id}
                        className={`group relative flex cursor-pointer items-center gap-4 overflow-hidden rounded-xl bg-linear-to-r ${song.color} to-card p-4 transition-all hover:scale-[1.02]`}
                    >
                        <img
                            src={song.image || "https://placehold.co/96x96"}
                            alt={song.title}
                            className="h-24 w-24 rounded-lg object-cover shadow-lg"
                        />
                        <div className="flex-1">
                            <h3 className="font-semibold text-foreground">{song.title}</h3>
                            <p className="text-sm text-muted-foreground">{song.artist}</p>
                        </div>
                        <button className="absolute bottom-4 right-4 flex h-12 w-12 translate-y-2 items-center justify-center rounded-full bg-primary text-primary-foreground opacity-0 shadow-lg transition-all group-hover:translate-y-0 group-hover:opacity-100">
                            <Play className="h-5 w-5 fill-current" />
                        </button>
                    </div>
                ))}
            </div>
        </section>
    )
}
