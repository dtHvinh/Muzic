import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Toaster } from "sonner";
import App from "./App.jsx";
import { AddArtistModalProvider } from "./context/AddArtistModalProvider";
import { RoutingContextProvider } from "./context/RoutingContextProvider";
import { AddSongModalProvider } from "./context/AddSongModalProvider";
import "./index.css";
import Layout from "./Layout.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RoutingContextProvider>
      <AddArtistModalProvider>
        <AddSongModalProvider>
          <Toaster richColors={true} />
          <Layout>
            <App />
          </Layout>
        </AddSongModalProvider>
      </AddArtistModalProvider>
    </RoutingContextProvider>
  </StrictMode>
);
