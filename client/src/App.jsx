import { useEffect } from "react";
import "./App.css";
import Fallback from "./components/core/fallback";
import Artist from "./components/pages/artist";
import ArtistEdit from "./components/pages/artist-edit";
import Home from "./components/pages/home";
import Liked from "./components/pages/liked";
import PlayList from "./components/pages/playlist";
import Search from "./components/pages/search";
import { useRoutingContext } from "./context/RoutingContext";
import './types/musicbrainz.d.js';

function App() {
  const {
    getRouteContent,
    currentRoute,
    setCurrentRoute,
    mapRoute,
    setFallback
  } = useRoutingContext();

  useEffect(() => {
    setFallback(<Fallback />)

    mapRoute('home', <Home />)
    mapRoute('search', <Search />)
    mapRoute('playlist', <PlayList />)
    mapRoute('liked', <Liked />)
    mapRoute('artist', <Artist />)
    mapRoute('artist/edit', <ArtistEdit />)
  }, [])

  return (
    getRouteContent()
  );
}

export default App;
