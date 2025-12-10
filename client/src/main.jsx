import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";
import { RoutingContextProvider } from "./context/RoutingContext";
import "./index.css";
import Layout from "./Layout.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <RoutingContextProvider >
      <Layout>
        <App />
      </Layout>
    </RoutingContextProvider>
  </StrictMode>
);
