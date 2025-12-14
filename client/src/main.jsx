import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Toaster } from "sonner";
import App from "./App.jsx";
import { AddArtistModalProvider } from "./context/AddArtistModalProvider";
import { RoutingContextProvider } from "./context/RoutingContextProvider";
import "./index.css";
import Layout from "./Layout.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RoutingContextProvider>
      <AddArtistModalProvider>
        <Toaster richColors={true} />
        <Layout>
          <App />
        </Layout>
      </AddArtistModalProvider>
    </RoutingContextProvider>
  </StrictMode>
);
