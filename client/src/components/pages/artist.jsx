import { useArtist } from "../../hooks/useArtist";
import { useSearchArtist } from "../../hooks/useSearchArtist";
import AddArtistButton from "../add-artist-button";
import ArtistCard, { SkeletonArtistCard } from "../artist-card";

export default function Artist() {
  const { artists, isLoading, reset } = useSearchArtist({
    offset: 0,
    limit: 42,
  });
  const { deleteArtist } = useArtist();

  return (
    <div>
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-7 space-y-5">
        <div className="flex">
          <AddArtistButton />
        </div>
        {artists.map((artist) => (
          <ArtistCard
            artist={artist}
            cardProps={{
              size: 24,
            }}
            onDelete={async () => {
              if (confirm(`Do you want to delete this artist`)) {
                await deleteArtist(artist.id);
                reset();
              }
            }}
          />
        ))}

        {artists.length === 0 && !isLoading && (
          <p className="text-center col-span-full mt-5 text-gray-400">
            No artists found
          </p>
        )}

        {isLoading &&
          Array(7)
            .fill()
            .map((_, i) => <SkeletonArtistCard key={i} />)}
      </div>
    </div>
  );
}
