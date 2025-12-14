import { useEffect } from "react";
import "./App.css";
import Fallback from "./components/core/fallback";
import { useRoutingContext } from "./hooks/useRoutingContext";
import Artist from "./pages/artist";
import ArtistEdit from "./pages/artist-edit";
import Home from "./pages/home";
import Liked from "./pages/liked";
import PlayList from "./pages/playlist";
import Search from "./pages/search";
import Songs from "./pages/songs";
import "./types/musicbrainz.d.js";

function App() {
  const { getRouteContent, mapRoute, setFallback } = useRoutingContext();

  useEffect(() => {
    setFallback(<Fallback />);

    mapRoute("home", <Home />);

    mapRoute("search", <Search />);

    mapRoute("playlist", <PlayList />);

    mapRoute("liked", <Liked />);

    mapRoute("artist", <Artist />);
    mapRoute("artist/edit", <ArtistEdit />);

    mapRoute("song", <Songs />);
  }, []);

  return getRouteContent();
}

export default App;
