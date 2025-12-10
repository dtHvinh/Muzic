import FeaturedSection from '../featured-section'
import ArtistsSection from '../popular-artist'

export default function Home() {
    return (
        <div className="space-y-8 px-6 pb-32">
            <FeaturedSection />
            <ArtistsSection />
            {/*    <RecentlyPlayed />
                <PlaylistsSection /> */}
        </div>
    )
}