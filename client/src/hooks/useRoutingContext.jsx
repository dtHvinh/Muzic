import { useContext } from "react";
import { RoutingContext } from "../context/RoutingContext";

export const useRoutingContext = () => {
  const ctx = useContext(RoutingContext);
  if (!ctx) {
    throw new Error(
      "useRoutingContext must be used inside <RoutingContextProvider>"
    );
  }
  return ctx;
};
