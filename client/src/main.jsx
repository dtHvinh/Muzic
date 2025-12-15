import dayjs from "dayjs";
import plugin from "dayjs/plugin/relativeTime";
import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Toaster } from "sonner";
import App from "./App.jsx";
import { AddArtistModalProvider } from "./context/AddArtistModalProvider";
import { AddPlaylistModalProvider } from "./context/AddPlaylistModalProvider.jsx";
import { AddSongModalProvider } from "./context/AddSongModalProvider";
import { RoutingContextProvider } from "./context/RoutingContextProvider";
import "./index.css";
import Layout from "./Layout.jsx";
dayjs.extend(plugin);

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RoutingContextProvider>
      <AddPlaylistModalProvider>
        <AddArtistModalProvider>
          <AddSongModalProvider>
            <Toaster richColors={true} />
            <Layout>
              <App />
            </Layout>
          </AddSongModalProvider>
        </AddArtistModalProvider>
      </AddPlaylistModalProvider>
    </RoutingContextProvider>
  </StrictMode>
);
