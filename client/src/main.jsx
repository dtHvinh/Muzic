import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Toaster } from "sonner";
import App from "./App.jsx";
import { AddArtistModalProvider } from "./context/AddArtistModalContext.jsx";
import { RoutingContextProvider } from "./context/RoutingContext";
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
