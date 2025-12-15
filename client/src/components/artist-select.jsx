import { Check, ChevronsUpDown, Search } from "lucide-react";
import React from "react";
import { useSearchArtist } from "../hooks/useSearchArtist";

const FALLBACK_GRADIENT = "https://api.dicebear.com/7.x/avataaars/svg";

const getArtistImage = (artist) => {
  if (!artist) return `${FALLBACK_GRADIENT}?seed=Artist`;
  const candidate =
    artist.profile_image || artist.image || artist.avatar || artist.photo;
  if (candidate) return candidate;
  const seed = encodeURIComponent(artist.name || "Artist");
  return `${FALLBACK_GRADIENT}?seed=${seed}`;
};

function ArtistSelectOption({ artist, isActive, onSelect }) {
  return (
    <button
      type="button"
      onClick={onSelect}
      className={`w-full rounded-2xl border border-transparent bg-white/5 px-3 py-2 text-left transition hover:border-white/20 hover:bg-white/10 ${
        isActive ? "border-primary/40 bg-primary/10" : ""
      }`}
    >
      <div className="flex items-center gap-3">
        <img
          src={getArtistImage(artist)}
          alt={artist.name}
          className="h-10 w-10 rounded-full object-cover"
        />
        <div className="flex-1">
          <p className="text-sm font-medium text-foreground">{artist.name}</p>
          <p className="text-xs text-muted-foreground">
            {artist.genre || "Artist"}
          </p>
        </div>
        {isActive && <Check className="h-4 w-4 text-primary" />}
      </div>
    </button>
  );
}

export default function ArtistSelect({
  value,
  onChange,
  label = "Artist",
  placeholder = "Select artist",
  error = "",
}) {
  const [searchTerm, setSearchTerm] = React.useState("");
  const [isOpen, setIsOpen] = React.useState(false);
  const [selectedArtist, setSelectedArtist] = React.useState(null);
  const containerRef = React.useRef(null);

  const { artists, isLoading } = useSearchArtist({
    query: searchTerm,
    offset: 0,
    limit: 50,
  });

  React.useEffect(() => {
    if (!value) {
      setSelectedArtist(null);
      return;
    }
    const match = artists.find((artist) => String(artist.id) === String(value));
    if (match) {
      setSelectedArtist(match);
    }
  }, [value, artists]);

  React.useEffect(() => {
    if (!isOpen) return;
    const handleClickOutside = (event) => {
      if (
        containerRef.current &&
        !containerRef.current.contains(event.target)
      ) {
        setIsOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  const handleSelect = (artist) => {
    setSelectedArtist(artist);
    onChange?.(String(artist.id));
    setIsOpen(false);
  };

  return (
    <div className="relative flex flex-col gap-2" ref={containerRef}>
      <span className="text-xs font-medium uppercase tracking-wide text-muted-foreground">
        {label}
      </span>

      <button
        type="button"
        onClick={() => setIsOpen((prev) => !prev)}
        className={`flex w-full items-center justify-between rounded-2xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-foreground shadow-inner transition focus:border-primary/60 focus:outline-none focus:ring-4 focus:ring-primary/15 ${
          error ? "border-red-500/60" : ""
        }`}
        aria-haspopup="listbox"
        aria-expanded={isOpen}
      >
        {selectedArtist ? (
          <div className="flex flex-1 items-center gap-3 text-left">
            <img
              src={getArtistImage(selectedArtist)}
              alt={selectedArtist.name}
              className="h-10 w-10 rounded-full object-cover"
            />
            <div>
              <p className="text-sm font-medium text-foreground">
                {selectedArtist.name}
              </p>
              <p className="text-xs text-muted-foreground">
                {selectedArtist.genre || "Artist"}
              </p>
            </div>
          </div>
        ) : (
          <span className="flex-1 text-left text-sm text-muted-foreground">
            {placeholder}
          </span>
        )}
        <ChevronsUpDown className="h-4 w-4 text-muted-foreground" />
      </button>

      {error && (
        <span className="text-xs text-red-400" role="alert">
          {error}
        </span>
      )}

      {isOpen && (
        <div className="absolute left-0 right-0 top-full z-30 mt-3 rounded-2xl border border-white/10 bg-[#0f131c] p-4 shadow-[0_20px_40px_-20px_rgba(15,23,42,0.8)]">
          <div className="flex items-center gap-2 rounded-2xl border border-white/10 bg-white/5 px-3 py-2">
            <Search className="h-4 w-4 text-muted-foreground" />
            <input
              type="text"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="w-full bg-transparent text-sm text-foreground placeholder:text-muted-foreground focus:outline-none"
              placeholder="Search artist..."
              autoFocus
            />
          </div>

          <div className="mt-3 max-h-64 space-y-2 overflow-y-auto pr-1">
            {isLoading && (
              <p className="text-sm text-muted-foreground">
                Loading artists...
              </p>
            )}

            {!isLoading && artists.length === 0 && (
              <p className="text-sm text-muted-foreground">
                No artists match "{searchTerm}".
              </p>
            )}

            {!isLoading &&
              artists.map((artist) => (
                <ArtistSelectOption
                  key={artist.id}
                  artist={artist}
                  isActive={String(artist.id) === String(value)}
                  onSelect={() => handleSelect(artist)}
                />
              ))}
          </div>
        </div>
      )}
    </div>
  );
}
