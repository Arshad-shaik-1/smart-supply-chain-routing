import { useEffect, useState } from "react";
import "./App.css";


function App() {

  const [locations, setLocations] = useState([]);
  const [activePage, setActivePage] = useState("Dashboard");

  const [vehicles, setVehicles] = useState([]);

  const [name, setName] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [type, setType] = useState("");

  const [vehicleNo, setVehicleNo] = useState("");
const [capacity, setCapacity] = useState("");
const [currentLocationId, setCurrentLocationId] = useState("");
const [status, setStatus] = useState("");

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

  if (activePage === "Locations" || activePage === "Vehicles") {
  fetchLocations();
}

  if (activePage === "Vehicles") {
    fetchVehicles();
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

      </main>

    </div>
  );
}

export default App;