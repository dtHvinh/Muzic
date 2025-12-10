import { useRoutingContext } from "../../context/RoutingContext";

export default function Artist() {
    const { getAllQueries } = useRoutingContext();

    return (
        <div>
            Artist {JSON.stringify(getAllQueries())}
        </div>
    )
}