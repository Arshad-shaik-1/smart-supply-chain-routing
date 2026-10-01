import { useEffect, useState } from "react";
import "./App.css";


function App() {

  const [locations, setLocations] = useState([]);
  const [activePage, setActivePage] = useState("Dashboard");
  const [deliveries, setDeliveries] = useState([]);

  const [sourceLocationId, setSourceLocationId] = useState("");
// const [destinationLocationId, setDestinationLocationId] = useState("");
const [routeResult, setRouteResult] = useState(null);
const [routeError, setRouteError] = useState("");

const [selectedRouteVehicleId, setSelectedRouteVehicleId] = useState("");
const [selectedDeliveryIds, setSelectedDeliveryIds] = useState([]);
const [multiRouteResult, setMultiRouteResult] = useState(null);
const [multiRouteError, setMultiRouteError] = useState("");

  const [vehicles, setVehicles] = useState([]);

  const [name, setName] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [type, setType] = useState("");

  const [vehicleNo, setVehicleNo] = useState("");
const [capacity, setCapacity] = useState("");
const [currentLocationId, setCurrentLocationId] = useState("");
const [status, setStatus] = useState("");

const [deliveryAddress, setDeliveryAddress] = useState("");
const [weight, setWeight] = useState("");
const [priority, setPriority] = useState("");
const [deliveryStatus, setDeliveryStatus] = useState("PENDING");
const [destinationLocationId, setDestinationLocationId] = useState("");
const [vehicleId, setVehicleId] = useState("");



 const fetchVehicles = () => {
  fetch("http://localhost:8080/api/vehicles")
    .then(response => {
      console.log("Vehicle response status:", response.status);
      return response.json();
    })
    .then(data => {
      console.log("Vehicle data:", data);
      setVehicles(data);
    })
    .catch(error => {
      console.error("Error fetching vehicles:", error);
    });
};

const fetchDeliveries = () => {
  fetch("http://localhost:8080/api/deliveries")
    .then(response => response.json())
    .then(data => {
      setDeliveries(data);
    })
    .catch(error => {
      console.error("Error fetching deliveries:", error);
    });
};



  const handleDeleteLocation = (id) => {

  fetch(`http://localhost:8080/api/locations/${id}`, {
    method: "DELETE"
  })
    .then(response => {

      if (!response.ok) {
        throw new Error("Failed to delete location");
      }

      fetchLocations();
    })
    .catch(error => {
      console.error("Error deleting location:", error);
    });
};

  const fetchLocations = () => {
    fetch("http://localhost:8080/api/locations")
      .then(response => response.json())
      .then(data => {
        setLocations(data);
      })
      .catch(error => {
        console.error("Error fetching locations:", error);
      });
  };

  useEffect(() => {

  if (
    activePage === "Locations" ||
    activePage === "Vehicles" ||
    activePage === "Deliveries" ||
    activePage === "Routes"
  ) {
    fetchLocations();
  }

  if (
    activePage === "Vehicles" ||
    activePage === "Deliveries" ||
    activePage === "Routes"
  ) {
    fetchVehicles();
  }

  if (
    activePage === "Deliveries" ||
    activePage === "Routes"
  ) {
    fetchDeliveries();
  }

}, [activePage]);

  const handleAddLocation = (event) => {

    event.preventDefault();

    const newLocation = {
      name: name,
      latitude: Number(latitude),
      longitude: Number(longitude),
      type: type
    };

    fetch("http://localhost:8080/api/locations", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(newLocation)
    })
      .then(response => {

        if (!response.ok) {
          throw new Error("Failed to add location");
        }

        return response.json();
      })
      .then(data => {

        console.log("Location added:", data);

        setName("");
        setLatitude("");
        setLongitude("");
        setType("");

        fetchLocations();
      })
      .catch(error => {
        console.error("Error adding location:", error);
      });
  };

  const handleAddVehicle = (event) => {

  event.preventDefault();

  const newVehicle = {
    vehicleNo: vehicleNo,
    capacity: Number(capacity),
    currentLocation: {
      id: Number(currentLocationId)
    },
    status: status
  };

  fetch("http://localhost:8080/api/vehicles", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(newVehicle)
  })
    .then(response => {

      if (!response.ok) {
        throw new Error("Failed to add vehicle");
      }

      return response.json();
    })
    .then(data => {

      console.log("Vehicle added:", data);

      setVehicleNo("");
      setCapacity("");
      setCurrentLocationId("");
      setStatus("");

      fetchVehicles();
    })
    .catch(error => {
      console.error("Error adding vehicle:", error);
    });
};

const handleDeleteVehicle = (id) => {

  fetch(`http://localhost:8080/api/vehicles/${id}`, {
    method: "DELETE"
  })
    .then(response => {

      if (!response.ok) {
        throw new Error("Failed to delete vehicle");
      }

      fetchVehicles();
    })
    .catch(error => {
      console.error("Error deleting vehicle:", error);
    });
};


const handleAddDelivery = (event) => {

  event.preventDefault();

  const newDelivery = {
    deliveryAddress: deliveryAddress,
    weight: Number(weight),
    priority: priority,
    status: deliveryStatus,
    destinationLocation: {
      id: Number(destinationLocationId)
    },
    vehicle: vehicleId
      ? { id: Number(vehicleId) }
      : null
  };

  fetch("http://localhost:8080/api/deliveries", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(newDelivery)
  })
    .then(response => {

      if (!response.ok) {
        throw new Error("Failed to add delivery");
      }

      return response.json();
    })
    .then(data => {

      console.log("Delivery added:", data);

      setDeliveryAddress("");
      setWeight("");
      setPriority("");
      setDeliveryStatus("PENDING");
      setDestinationLocationId("");
      setVehicleId("");

      fetchDeliveries();
    })
    .catch(error => {
      console.error("Error adding delivery:", error);
    });
};

const handleDeleteDelivery = (id) => {

  fetch(`http://localhost:8080/api/deliveries/${id}`, {
    method: "DELETE"
  })
    .then(response => {

      if (!response.ok) {
        throw new Error("Failed to delete delivery");
      }

      fetchDeliveries();
    })
    .catch(error => {
      console.error("Error deleting delivery:", error);
    });
};

const handleFindRoute = (event) => {
  event.preventDefault();

  setRouteResult(null);
  setRouteError("");

  fetch(
    `http://localhost:8080/api/graph/shortest-path?source=${sourceLocationId}&destination=${destinationLocationId}`
  )
    .then(response => {
      if (!response.ok) {
        return response.json().then(errorData => {
          throw new Error(errorData.error || "Failed to find route");
        });
      }

      return response.json();
    })
    .then(data => {
      setRouteResult(data);
    })
    .catch(error => {
      setRouteError(error.message);
    });
};

const handleCalculateMultiRoute = (event) => {
  event.preventDefault();

  setMultiRouteResult(null);
  setMultiRouteError("");

  const request = {
    vehicleId: Number(selectedRouteVehicleId),
    deliveryIds: selectedDeliveryIds.map(id => Number(id))
  };

  fetch("http://localhost:8080/api/routes/multi-delivery", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(request)
  })
    .then(response => {
      if (!response.ok) {
        return response.json().then(errorData => {
          throw new Error(
            errorData.error || "Failed to calculate multi-delivery route"
          );
        });
      }

      return response.json();
    })
    .then(data => {
      setMultiRouteResult(data);
    })
    .catch(error => {
      setMultiRouteError(error.message);
    });
};


const handleDeliverySelection = (deliveryId) => {

  setSelectedDeliveryIds(previousIds => {

    if (previousIds.includes(deliveryId)) {
      return previousIds.filter(id => id !== deliveryId);
    }

    return [...previousIds, deliveryId];

  });

};

  return (
    <div className="app">

      <aside className="sidebar">

        <h2>Supply Chain</h2>

        <nav>

          <button onClick={() => setActivePage("Dashboard")}>
            Dashboard
          </button>

          <button onClick={() => setActivePage("Locations")}>
            Locations
          </button>

          <button onClick={() => setActivePage("Vehicles")}>
            Vehicles
          </button>

          <button onClick={() => setActivePage("Deliveries")}>
            Deliveries
          </button>

          <button onClick={() => setActivePage("Routes")}>
            Routes
          </button>

        </nav>

      </aside>

      {activePage === "Routes" && (
        
  <section>
    <header className="header">
      <h1>Route Calculator</h1>
      <p>Find the shortest route between two locations</p>
    </header>

    <div className="route-form-card">

      <h2>Calculate Shortest Route</h2>

      <form onSubmit={handleFindRoute}>

        <select
          value={sourceLocationId}
          onChange={(event) => setSourceLocationId(event.target.value)}
          required
        >
          <option value="">Select source location</option>

          {locations.map(location => (
            <option
              key={location.id}
              value={location.id}
            >
              {location.name}
            </option>
          ))}

        </select>

        <select
          value={destinationLocationId}
          onChange={(event) => setDestinationLocationId(event.target.value)}
          required
        >
          <option value="">Select destination location</option>

          {locations.map(location => (
            <option
              key={location.id}
              value={location.id}
            >
              {location.name}
            </option>
          ))}

        </select>

        <button type="submit">
          Find Shortest Route
        </button>

      </form>

    </div>

    {routeError && (
      <div className="route-error">
        <p>{routeError}</p>
      </div>
    )}

    {routeResult && (
      <div className="route-result">

        <h2>Route Result</h2>

        <p>
          <strong>Source:</strong>{" "}
          {locations.find(
            location => location.id === routeResult.source
          )?.name}
        </p>

        <p>
          <strong>Destination:</strong>{" "}
          {locations.find(
            location => location.id === routeResult.destination
          )?.name}
        </p>

        <p>
          <strong>Distance:</strong>{" "}
          {routeResult.distance}
        </p>

        <p>
          <strong>Path:</strong>{" "}
          {routeResult.path.join(" → ")}
        </p>

      </div>
    )}


    <div className="multi-route-section">

  <header className="header">
    <h1>Multi-Delivery Route</h1>
    <p>Calculate an optimized route for multiple deliveries</p>
  </header>

  <div className="multi-route-card">

    <h2>Select Vehicle</h2>

    <form onSubmit={handleCalculateMultiRoute}>

      <select
        value={selectedRouteVehicleId}
        onChange={(event) =>
          setSelectedRouteVehicleId(event.target.value)
        }
        required
      >
        <option value="">Select vehicle</option>

        {vehicles.map(vehicle => (
          <option
            key={vehicle.id}
            value={vehicle.id}
          >
            {vehicle.vehicleNo} - Capacity: {vehicle.capacity}
          </option>
        ))}

      </select>

      <h2>Select Deliveries</h2>

      <div className="route-delivery-list">

        {deliveries.length === 0 ? (
          <p>No deliveries available.</p>
        ) : (

          deliveries.map(delivery => (

            <label
              key={delivery.id}
              className="route-delivery-item"
            >

              <input
                type="checkbox"
                checked={selectedDeliveryIds.includes(delivery.id)}
                onChange={() =>
                  handleDeliverySelection(delivery.id)
                }
              />

              <span>
                Delivery #{delivery.id} —{" "}
                {delivery.deliveryAddress}
                {" | "}
                Weight: {delivery.weight}
                {" | "}
                Priority: {delivery.priority}
              </span>

            </label>

          ))

        )}

      </div>

      <button type="submit">
        Calculate Optimized Route
      </button>

    </form>

  </div>

  {multiRouteError && (
    <div className="route-error">
      <p>{multiRouteError}</p>
    </div>
  )}

  {multiRouteResult && (
    <div className="multi-route-result">

      <h2>Optimized Route</h2>

      <p>
        <strong>Vehicle:</strong>{" "}
        {vehicles.find(
          vehicle => vehicle.id === multiRouteResult.vehicleId
        )?.vehicleNo}
      </p>

      <p>
        <strong>Total Distance:</strong>{" "}
        {multiRouteResult.totalDistance}
      </p>

      <h3>Delivery Order</h3>

      <ol>
        {multiRouteResult.deliveryIds.map(deliveryId => {

          const delivery = deliveries.find(
            item => item.id === deliveryId
          );

          return (
            <li key={deliveryId}>
              Delivery #{deliveryId}
              {" - "}
              {delivery?.deliveryAddress}
            </li>
          );

        })}
      </ol>

      <h3>Routes</h3>

      {multiRouteResult.routes.map((route, index) => (

        <div
          className="individual-route"
          key={index}
        >

          <p>
            <strong>Route {index + 1}</strong>
          </p>

          <p>
            From: {route.source}
          </p>

          <p>
            To: {route.destination}
          </p>

          <p>
            Distance: {route.distance}
          </p>

          <p>
            Path: {route.path.join(" → ")}
          </p>

        </div>

      ))}

    </div>
  )}

</div>

  </section>
)}

      <main className="main-content">

        {activePage === "Dashboard" && (
          <>
            <header className="header">
              <h1>Smart Supply Chain Routing</h1>
              <p>Manage deliveries, vehicles and routes</p>
            </header>

            <section className="stats">

              <div className="card">
                <h3>Locations</h3>
                <p>{locations.length}</p>
              </div>

              <div className="card">
                <h3>Vehicles</h3>
                <p>0</p>
              </div>

              <div className="card">
                <h3>Deliveries</h3>
                <p>0</p>
              </div>

              <div className="card">
                <h3>Active Routes</h3>
                <p>0</p>
              </div>

            </section>

            <section className="welcome">
              <h2>Welcome to the Dashboard</h2>
              <p>
                Your supply chain routing system will appear here.
              </p>
            </section>
          </>
        )}

        {activePage === "Locations" && (
          <section>

            <header className="header">
              <h1>Locations</h1>
              <p>Manage supply chain locations</p>
            </header>

            <div className="add-location">

              <h2>Add Location</h2>

              <form onSubmit={handleAddLocation}>

                <input
                  type="text"
                  placeholder="Location name"
                  value={name}
                  onChange={(event) => setName(event.target.value)}
                  required
                />

                <input
                  type="number"
                  step="any"
                  placeholder="Latitude"
                  value={latitude}
                  onChange={(event) => setLatitude(event.target.value)}
                  required
                />

                <input
                  type="number"
                  step="any"
                  placeholder="Longitude"
                  value={longitude}
                  onChange={(event) => setLongitude(event.target.value)}
                  required
                />

               <select
  value={type}
  onChange={(event) => setType(event.target.value)}
  required
>
  <option value="">Select location type</option>
  <option value="WAREHOUSE">Warehouse</option>
  <option value="DELIVERY_POINT">Delivery Point</option>
</select>

                <button type="submit">
                  Add Location
                </button>

              </form>

            </div>

            <div className="location-list">

              {locations.length === 0 ? (
                <p>No locations found.</p>
              ) : (

                locations.map(location => (

                  <div className="location-card" key={location.id}>

  <h3>{location.name}</h3>

  <p>Location ID: {location.id}</p>

  <p>
    Latitude: {location.latitude}
  </p>

  <p>
    Longitude: {location.longitude}
  </p>

  <p>
    Type: {location.type}
  </p>

  <button
    className="delete-button"
    onClick={() => handleDeleteLocation(location.id)}
  >
    Delete
  </button>

</div>
                ))

              )}

            </div>

          </section>
        )}

        {activePage === "Vehicles" && (
  <section>

    <header className="header">
      <h1>Vehicles</h1>
      <p>Manage supply chain vehicles</p>
    </header>
          <div className="add-vehicle">

  <h2>Add Vehicle</h2>

  <form onSubmit={handleAddVehicle}>

    <input
      type="text"
      placeholder="Vehicle number"
      value={vehicleNo}
      onChange={(event) => setVehicleNo(event.target.value)}
      required
    />

    <input
      type="number"
      step="any"
      placeholder="Capacity"
      value={capacity}
      onChange={(event) => setCapacity(event.target.value)}
      required
    />

    <select
      value={currentLocationId}
      onChange={(event) => setCurrentLocationId(event.target.value)}
      required
    >
      <option value="">Select current location</option>

      {locations.map(location => (
        <option
          key={location.id}
          value={location.id}
        >
          {location.name}
        </option>
      ))}

    </select>

    <select
      value={status}
      onChange={(event) => setStatus(event.target.value)}
      required
    >
      <option value="">Select status</option>
      <option value="AVAILABLE">Available</option>
      <option value="UNAVAILABLE">Unavailable</option>
      <option value="MAINTENANCE">Maintenance</option>
    </select>

    <button type="submit">
      Add Vehicle
    </button>

  </form>

</div>
    <div className="vehicle-list">

      {vehicles.length === 0 ? (
        <p>No vehicles found.</p>
      ) : (

        vehicles.map(vehicle => (

          <div className="vehicle-card" key={vehicle.id}>

            <h3>{vehicle.vehicleNo}</h3>

            <p>
              Vehicle ID: {vehicle.id}
            </p>

            <p>
              Capacity: {vehicle.capacity}
            </p>

            <p>
              Current Location:{" "}
              {vehicle.currentLocation
                ? vehicle.currentLocation.name
                : "Not assigned"}
            </p>

            <p>
              Status: {vehicle.status}
              <button
  className="delete-button"
  onClick={() => handleDeleteVehicle(vehicle.id)}
>
  Delete
</button>
            </p>

          </div>

        ))

      )}

    </div>

  </section>
)}

{activePage === "Deliveries" && (
  <section>

    <header className="header">
      <h1>Deliveries</h1>
      <p>Manage supply chain deliveries</p>
    </header>

<div className="add-delivery">

  <h2>Add Delivery</h2>

  <form onSubmit={handleAddDelivery}>

    <input
      type="text"
      placeholder="Delivery address"
      value={deliveryAddress}
      onChange={(event) => setDeliveryAddress(event.target.value)}
      required
    />

    <input
      type="number"
      step="any"
      placeholder="Weight"
      value={weight}
      onChange={(event) => setWeight(event.target.value)}
      required
    />

    <select
      value={priority}
      onChange={(event) => setPriority(event.target.value)}
      required
    >
      <option value="">Select priority</option>
      <option value="LOW">Low</option>
      <option value="MEDIUM">Medium</option>
      <option value="HIGH">High</option>
    </select>

    <select
      value={destinationLocationId}
      onChange={(event) => setDestinationLocationId(event.target.value)}
      required
    >
      <option value="">Select destination</option>

      {locations.map(location => (
        <option
          key={location.id}
          value={location.id}
        >
          {location.name}
        </option>
      ))}

    </select>

    <select
      value={vehicleId}
      onChange={(event) => setVehicleId(event.target.value)}
    >
      <option value="">No vehicle / Assign later</option>

      {vehicles.map(vehicle => (
        <option
          key={vehicle.id}
          value={vehicle.id}
        >
          {vehicle.vehicleNo}
        </option>
      ))}

    </select>

    <select
      value={deliveryStatus}
      onChange={(event) => setDeliveryStatus(event.target.value)}
      required
    >
      <option value="PENDING">Pending</option>
      <option value="ASSIGNED">Assigned</option>
      <option value="IN_TRANSIT">In Transit</option>
      <option value="DELIVERED">Delivered</option>
      <option value="CANCELLED">Cancelled</option>
    </select>

    <button type="submit">
      Add Delivery
    </button>

  </form>

</div>

    <div className="delivery-list">

      {deliveries.length === 0 ? (
        <p>No deliveries found.</p>
      ) : (

        deliveries.map(delivery => (

  <div className="delivery-card" key={delivery.id}>

    <h3>
      Delivery #{delivery.id}
    </h3>

    <p>
      Address: {delivery.deliveryAddress}
    </p>

    <p>
      Weight: {delivery.weight}
    </p>

    <p>
      Priority: {delivery.priority}
    </p>

    <p>
      Status: {delivery.status}
    </p>

    <p>
      Destination:{" "}
      {delivery.destinationLocation
        ? delivery.destinationLocation.name
        : "Not assigned"}
    </p>

    <p>
      Vehicle:{" "}
      {delivery.vehicle
        ? delivery.vehicle.vehicleNo
        : "Not assigned"}
    </p>
    <button
  className="delete-button"
  onClick={() => handleDeleteDelivery(delivery.id)}
>
  Delete
</button>git
  </div>

))

      )}

    </div>

  </section>
)}

      </main>

    </div>
  );
}

export default App;