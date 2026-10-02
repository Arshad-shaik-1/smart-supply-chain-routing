import { useEffect, useState } from "react";

import {
    MapContainer,
    TileLayer,
    Marker,
    Popup,
    Polyline,
    useMap
} from "react-leaflet";

import "leaflet/dist/leaflet.css";


// --------------------------------------------------
// Helper component to automatically fit the map
// --------------------------------------------------

function MapBounds({ positions }) {

    const map = useMap();

    useEffect(() => {

        if (positions.length === 0) {
            return;
        }

        map.fitBounds(positions, {
            padding: [50, 50]
        });

    }, [positions, map]);

    return null;
}


// --------------------------------------------------
// Main Map Component
// --------------------------------------------------

function MapView({
    locations,
    routeResult,
    multiRouteResult
}) {

    const [roadPath, setRoadPath] = useState([]);

    const [mapPathIds, setMapPathIds] = useState([]);


    // --------------------------------------------------
    // Decide which route should be displayed
    // --------------------------------------------------

    useEffect(() => {

        // ----------------------------------------------
        // MULTI-DELIVERY ROUTE
        // ----------------------------------------------

        if (
            multiRouteResult &&
            multiRouteResult.routes &&
            multiRouteResult.routes.length > 0
        ) {

            const combinedPath = [];

            multiRouteResult.routes.forEach(route => {

                route.path.forEach(locationId => {

                    if (
                        combinedPath.length === 0 ||
                        combinedPath[combinedPath.length - 1] !== locationId
                    ) {
                        combinedPath.push(locationId);
                    }

                });

            });

            setMapPathIds(combinedPath);

            return;
        }


        // ----------------------------------------------
        // SINGLE ROUTE
        // ----------------------------------------------

        if (
            routeResult &&
            routeResult.path &&
            routeResult.path.length > 0
        ) {

            setMapPathIds(routeResult.path);

            return;
        }


        // ----------------------------------------------
        // NO ROUTE
        // ----------------------------------------------

        setMapPathIds([]);

    }, [routeResult, multiRouteResult]);


    // --------------------------------------------------
    // Get road-following geometry
    // --------------------------------------------------

    useEffect(() => {

        if (
            locations.length === 0 ||
            mapPathIds.length < 2
        ) {

            setRoadPath([]);

            return;
        }


        // --------------------------------------------------
        // MULTI DELIVERY
        // --------------------------------------------------

        if (
            multiRouteResult &&
            multiRouteResult.routes &&
            multiRouteResult.routes.length > 0
        ) {

            const fetchRouteLegs = async () => {

                const allRoadCoordinates = [];

                try {

                    for (const route of multiRouteResult.routes) {

                        const routeLocations = route.path
                            .map(id =>
                                locations.find(
                                    location => location.id === id
                                )
                            )
                            .filter(Boolean);


                        if (routeLocations.length < 2) {
                            continue;
                        }


                        // OSRM:
                        // longitude,latitude

                        const coordinates = routeLocations
                            .map(location =>
                                `${location.longitude},${location.latitude}`
                            )
                            .join(";");


                        const url =
                            `https://router.project-osrm.org/route/v1/driving/` +
                            `${coordinates}` +
                            `?overview=full&geometries=geojson`;


                        const response = await fetch(url);


                        if (!response.ok) {
                            throw new Error(
                                "Failed to fetch road route"
                            );
                        }


                        const data = await response.json();


                        if (
                            data.code !== "Ok" ||
                            !data.routes ||
                            data.routes.length === 0
                        ) {

                            throw new Error(
                                "No road route found"
                            );

                        }


                        const legCoordinates =
                            data.routes[0].geometry.coordinates
                                .map(coordinate => [
                                    coordinate[1],
                                    coordinate[0]
                                ]);


                        // Avoid duplicate point where
                        // one route leg joins the next

                        if (allRoadCoordinates.length === 0) {

                            allRoadCoordinates.push(
                                ...legCoordinates
                            );

                        } else {

                            allRoadCoordinates.push(
                                ...legCoordinates.slice(1)
                            );

                        }

                    }


                    setRoadPath(allRoadCoordinates);

                } catch (error) {

                    console.error(
                        "OSRM multi-route error:",
                        error
                    );

                    setRoadPath([]);

                }

            };


            fetchRouteLegs();

            return;
        }


        // --------------------------------------------------
        // SINGLE ROUTE
        // --------------------------------------------------

        const routeLocations = mapPathIds
            .map(id =>
                locations.find(
                    location => location.id === id
                )
            )
            .filter(Boolean);


        if (routeLocations.length < 2) {

            setRoadPath([]);

            return;
        }


        const coordinates = routeLocations
            .map(location =>
                `${location.longitude},${location.latitude}`
            )
            .join(";");


        const url =
            `https://router.project-osrm.org/route/v1/driving/` +
            `${coordinates}` +
            `?overview=full&geometries=geojson`;


        fetch(url)

            .then(response => {

                if (!response.ok) {
                    throw new Error(
                        "Failed to fetch road route"
                    );
                }

                return response.json();

            })

            .then(data => {

                if (
                    data.code !== "Ok" ||
                    !data.routes ||
                    data.routes.length === 0
                ) {

                    throw new Error(
                        "No road route found"
                    );

                }


                const roadCoordinates =
                    data.routes[0].geometry.coordinates
                        .map(coordinate => [
                            coordinate[1],
                            coordinate[0]
                        ]);


                setRoadPath(roadCoordinates);

            })

            .catch(error => {

                console.error(
                    "OSRM route error:",
                    error
                );

                setRoadPath([]);

            });

    }, [
        mapPathIds,
        locations,
        multiRouteResult
    ]);


    // --------------------------------------------------
    // All location markers
    // --------------------------------------------------

    const markerPositions = locations
        .map(location => [
            location.latitude,
            location.longitude
        ]);


    // --------------------------------------------------
    // Route positions for fitting map
    // --------------------------------------------------

    const routePositions = mapPathIds
        .map(id =>
            locations.find(
                location => location.id === id
            )
        )
        .filter(Boolean)
        .map(location => [
            location.latitude,
            location.longitude
        ]);


    // --------------------------------------------------
    // Render
    // --------------------------------------------------

    return (

        <MapContainer
            center={[13.6288, 79.4192]}
            zoom={13}
            style={{
                height: "500px",
                width: "100%"
            }}
        >

            <TileLayer
                attribution='&copy; OpenStreetMap contributors'
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />


            {/* -------------------------------------- */}
            {/* Location markers */}
            {/* -------------------------------------- */}

            {locations.map(location => (

                <Marker
                    key={location.id}
                    position={[
                        location.latitude,
                        location.longitude
                    ]}
                >

                    <Popup>

                        <strong>
                            {location.name}
                        </strong>

                        <br />

                        Location ID: {location.id}

                        <br />

                        Type: {location.type}

                    </Popup>

                </Marker>

            ))}


            {/* -------------------------------------- */}
            {/* Road-following route */}
            {/* -------------------------------------- */}

            {roadPath.length > 1 && (

                <Polyline
                    positions={roadPath}
                    pathOptions={{
                        color: "blue",
                        weight: 5
                    }}
                />

            )}


            {/* -------------------------------------- */}
            {/* Automatically fit map */}
            {/* -------------------------------------- */}

            <MapBounds
                positions={
                    roadPath.length > 1
                        ? roadPath
                        : routePositions.length > 0
                            ? routePositions
                            : markerPositions
                }
            />

        </MapContainer>

    );

}

export default MapView;