import { useCallback } from "react";
import { useRoutingContext } from "../../hooks/useRoutingContext";

export default function Link({ href, target, children, onClick, className }) {
  const { navigateTo } = useRoutingContext();

  const clickFn = useCallback(() => {
    onClick();
    navigateTo(href);
  }, [navigateTo, href, onClick]);

  return (
    <button
      type="button"
      className={className}
      target={target}
      onClick={clickFn}
    >
      {children}
    </button>
  );
}
