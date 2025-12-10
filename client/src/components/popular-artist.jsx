import { useRoutingContext } from "../context/RoutingContext";

const artists = [
  {
    id: 1,
    name: "Luna Nova",
    genre: "Electronic",
    followers: "2.5M",
  },
  {
    id: 2,
    name: "The Voltage",
    genre: "Rock",
    followers: "1.8M",
  },
  {
    id: 3,
    name: "Jay Storm",
    genre: "Hip Hop",
    followers: "4.2M",
  },
  {
    id: 4,
    name: "Aria Belle",
    genre: "Pop",
    followers: "3.1M",
  },
  {
    id: 5,
    name: "Sunset Collective",
    genre: "Indie",
    followers: "890K",
  },
  {
    id: 6,
    name: "DJ Phoenix",
    genre: "House",
    followers: "1.2M",
  },
];

export default function ArtistsSection() {
  const { navigateTo } = useRoutingContext();

  return (
    <section>
      <div className="mb-4 flex items-center justify-between">
        <h2 className="text-2xl font-bold text-foreground">Popular Artists</h2>
        <button
          onClick={() =>
            navigateTo("artist", {
              term: "popular",
            })
          }
          className="text-sm font-medium text-muted-foreground transition-colors hover:text-primary"
        >
          See all
        </button>
      </div>
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6">
        {artists.map((artist) => (
          <div key={artist.id} className="group cursor-pointer text-center">
            <div className="relative mx-auto mb-3 h-64 w-64 overflow-hidden rounded-full">
              <img
                src={artist.image || "https://placehold.co/56x56"}
                alt={artist.name}
                className="aspect-square w-full object-cover transition-all group-hover:scale-105"
              />
            </div>
            <h3 className="truncate font-medium text-foreground">
              {artist.name}
            </h3>
            <p className="text-xs text-muted-foreground">
              {artist.genre} • {artist.followers} followers
            </p>
          </div>
        ))}
      </div>
    </section>
  );
}
