package BusinessLayer.ShipmentsModule;

import BusinessLayer.ShipmentsModule.Controllers.DriverController;
import BusinessLayer.ShipmentsModule.Controllers.LocationController;
import BusinessLayer.ShipmentsModule.Controllers.ShipmentController;
import BusinessLayer.ShipmentsModule.Controllers.TruckController;
import BusinessLayer.ShipmentsModule.Objects.*;
import DTOPackage.*;

import java.util.*;

public class Facade {
    private DriverController driverController;
    private TruckController truckController;
    private LocationController locationController;
    private ShipmentController shipmentController;

    public Facade() {
        this.driverController = new DriverController();
        this.truckController = new TruckController();
        this.locationController = new LocationController();
        this.shipmentController = new ShipmentController();
    }

    /**
     * @param truckId - the requested truck unique id
     * @return response of type TruckDTO by the given id
     */
    public ResponseT<TruckDTO> getTruckDTO(String truckId) {
        try {
            return new ResponseT<>(new TruckDTO(truckController.getTruck(truckId)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * @param id - the requested driver unique id
     * @return response of type DriverDTO by the given id
     */
    public ResponseT<DriverDTO> getDriverDTO(String id) {
        try {
            return new ResponseT<>(Builder.buildDTO(driverController.getDriver(id)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * @param addressId - the requested location unique address
     * @return response of type LocationDTO by the given id
     */
    public ResponseT<LocationDTO> getLocationDTO(int addressId) {
        try {
            return new ResponseT<>(Builder.buildDTO(locationController.getLocation(addressId)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * Adding a new location to the system
     *
     * @param address     - location unique id
     * @param phoneNumber - phone number of the person to contact
     * @param contactName - the name of the person to contact
     * @return response contains msg in case of any error
     */
    public Response addLocation(String address, String phoneNumber, String contactName) {
        try {
            locationController.addLocation(address, phoneNumber, contactName);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * @param truckPlateNumber - truck's plate number unique id
     * @param model            - Trucks model
     * @param natoWeight       - Truck's nato weight without any shipments
     * @param maxWeight        - The maximum weight which the truck can transport
     * @return response contains msg in case of any error
     */
    public Response addTruck(String truckPlateNumber, String model, double natoWeight, double maxWeight) {
        try {
            truckController.addTruck(truckPlateNumber, model, natoWeight, maxWeight);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * Adds a driver to the system
     *
     * @param id            - The unique id of the requested driver
     * @param name          - The driver's name
     * @param allowedWeight - The maximum weight he can transport in a delivery
     * @return response contains msg in case of any error
     */
    public Response addDriver(String id, String name, double allowedWeight) {
        try {
            driverController.addDriver(id, name, allowedWeight);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * @return Response of type List<DTO> containing all trucks in the system
     */
    public ResponseT<List<TruckDTO>> getAllTrucks() {
        try {
            List<Truck> trucks = truckController.getAllTrucks();
            return new ResponseT<>(Builder.buildTrucksListDTO(trucks));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * @return all drivers in the system
     */
    public ResponseT<List<DriverDTO>> getAllDrivers() {
        try {
            List<Driver> drivers = driverController.getAllDrivers();
            return new ResponseT<>(Builder.buildDriversListDTO(drivers));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * Arranges a delivery (adding a new delivery) to the system
     *
     * @param date               - Date of the shipment to be transported
     * @param departureHour      - The exact hour for the transportation of the shipment
     * @param sourceId           - The source's address unique id
     * @param items_per_location - Map[ItemName, List[ItemWeight, Quantity]] foreach location
     * @return response of type msg in case of any error
     */
    public Response arrangeDelivery(Date date, String departureHour, int sourceId, Map<Integer, List<ItemDTO>> items_per_location) {
        try {
            double shipmentWeight = 0;
            for (Integer loc : items_per_location.keySet()) {
                for (ItemDTO item : items_per_location.get(loc)) {
                    shipmentWeight += item.getWeight() * item.getAmount();
                }
            }
            Truck truck = truckController.getAvailableTruck(shipmentWeight);
            Driver driver = driverController.getAvailableDriver(weighTruck(truck.getTruckPlateNumber(),
                    shipmentWeight), date, departureHour);
            Location source = locationController.getLocation(sourceId);
            Map<Location, List<Item>> items = new HashMap<>();
            for (int loc : items_per_location.keySet()) {
                if (loc == sourceId) {
                    return new Response("Cannot deliver to destination as the same source, shipment removed.");
                }
                items.put(locationController.getLocation(loc), Builder.buildItemsList(items_per_location.get(loc)));
            }
            //TODO -> The id is 222222222 till we update the function of get available driver
            int shipmentId = shipmentController.addShipment(date, departureHour, truck.getTruckPlateNumber(), "222222222", source);
            for(Location loc: items.keySet()){
                shipmentController.addDocument(shipmentId, loc.getId(), items.get(loc));
            }
            truckController.makeUnavailableTruck(truck.getTruckPlateNumber());
            driverController.makeUnavailableDriver("222222222");
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    /**
     * Weighs the requested truck as requested before transportation
     *
     * @param truckId - The truck's unique plate number
     * @return the truck's total weight
     * @throws Exception in case of any error occurs
     * @throws Exception in case of any error occurs
     */
    private double weighTruck(String truckId, double shipmentWeight) throws Exception {
        double realWeight = truckController.getTruck(truckId).getNatoWeight();
        realWeight += shipmentWeight;
        if (realWeight > truckController.getTruck(truckId).getMaxWeight() + truckController.getTruck(truckId).getNatoWeight()) {
            throw new Exception("Truck's weight exceeded the limit, shipment has been removed.");
        }
        return realWeight;
    }

    /**
     * @return all locations in the system
     */
    public ResponseT<List<LocationDTO>> getAllLocations() {
        try {
            List<Location> locations = locationController.getAllLocations();
            return new ResponseT<>(Builder.buildLocationsListDTO(locations));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * @return all the shipments in the system
     */
    public ResponseT<List<ShipmentDTO>> getAllShipments() {
        try {
            List<Shipment> shipments = shipmentController.getAllShipments();
            return new ResponseT<>(Builder.buildShipmentsListDTO(shipments));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * Getting a shipment by its unique Ids
     *
     * @param date          - Date of the shipment to be transported
     * @param departureHour - The exact hour for the transportation of the shipment
     * @param driverId      - The driver's unique id
     * @return a response of type ShipmentDTO (the requested shipment)
     */
    public ResponseT<ShipmentDTO> getShipmentDTO(Date date, String departureHour, String driverId) {
        try {
            return new ResponseT<>(Builder.buildDTO(shipmentController.getShipment(date, departureHour, driverId)));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * A shipment follow to view it to the user
     *
     * @param trackingId - the tracking unique number foreach document(sub-shipment)
     * @return response of type ShipmentDTO in order to use in PL
     */
    public ResponseT<ShipmentDTO> trackShipment(int trackingId) {
        try {
            Shipment shipment = shipmentController.trackShipment(trackingId);
            return new ResponseT<>(Builder.buildDTO(shipment));
        } catch (Exception e) {
            return new ResponseT<>(e.getMessage());
        }
    }

    /**
     * Allows removing shipment from the system
     *
     * @param date          - Date of the shipment to be transported
     * @param departureHour - The exact hour for the transportation of the shipment
     * @param driverId      - The driver's unique id
     * @return response of type msg in case of any error
     */
    public Response removeShipment(Date date, String departureHour, String driverId) {
        try {
            Shipment shipment = shipmentController.getShipment(date, departureHour, driverId);
            truckController.depositTruck(shipment.getTruckPlateNumber());
            driverController.freeDriver(driverId);
            shipmentController.deleteShipment(shipment.getShipmentId());

            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}