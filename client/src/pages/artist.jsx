import AddArtistButton from "../components/add-artist-button";
import ArtistCard, { SkeletonArtistCard } from "../components/artist-card";
import { useArtist } from "../hooks/useArtist";
import { useSearchArtist } from "../hooks/useSearchArtist";
import SubLayout from "../layouts/sublayout";

export default function Artist() {
  const { artists, isLoading, reset } = useSearchArtist({
    offset: 0,
    limit: 50,
  });
  const { deleteArtist } = useArtist();

  return (
    <SubLayout>
      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-4xl font-bold text-white mb-2">Artists</h1>
          <p className="text-gray-600 dark:text-gray-600">
            Explore your favorite artist
          </p>
        </div>
        <AddArtistButton onAdd={() => reset()} />
      </div>
      <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-6 space-y-5">
        {artists.map((artist) => (
          <ArtistCard
            key={artist.id}
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
    </SubLayout>
  );
}
