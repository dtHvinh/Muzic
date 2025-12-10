import { useCallback } from "react";
import { useRoutingContext } from "../../context/RoutingContext";

export default function Link({ href, target, children, onClick, className }) {
    const { navigateTo } = useRoutingContext();

    const clickFn = useCallback(() => {
        onClick()
        navigateTo(href)
    })

    return (
        <button type="button" className={className} target={target} onClick={clickFn}>
            {children}
        </button>
    )
}