package BusinessLayer.ShipmentsModule.Controllers;

import BusinessLayer.ShipmentsModule.Builder;
import BusinessLayer.ShipmentsModule.Objects.Truck;
import DataAccessLayer.ShipmentsModule.Mappers.TruckMapper;

import java.util.List;

public class TruckController {
    private TruckMapper mapper;

    public TruckController() {
        mapper = TruckMapper.getInstance();
    }


    /***
     * Searching for the needed truck
     * @param truckID - Truck unique ID
     * @return Truck which has the given ID
     * @throws Exception in case truck not found in the system
     */
    public Truck getTruck(String truckID) throws Exception {
        return Builder.build(mapper.getTruck(truckID));
    }

    /***
     * Adding a new truck of the system
     * @param truckPlateNumber - Truck unique ID
     * @param model - Truck's model
     * @param natoWeight - Truck's weight without the shipment
     * @param maxWeight - The maximum possible weight of the shipment that truck can transport
     * @throws Exception in case of invalid parameters
     */
    public void addTruck(String truckPlateNumber, String model, double natoWeight, double maxWeight) throws Exception {
        if (maxWeight <= 0 || natoWeight <= 0)
            throw new Exception("Couldn't add new truck - Illegal truck weight");
        if (truckPlateNumber == null || truckPlateNumber.isEmpty() || model == null || model.isEmpty())
            throw new Exception("Couldn't add new truck - Invalid parameters");
        mapper.addTruck(truckPlateNumber, model, natoWeight, maxWeight, true);
    }

    /**
     * @return all trucks in the system
     */
    public List<Truck> getAlltrucks() {
        return null; //TODO
    }

    /**
     * @param weight - The shipment weight
     * @return an available truck which can transport a delivery of weight @param-weight
     * @throws Exception
     */
    public Truck getAvailableTruck(double weight) throws Exception {
        return Builder.build(mapper.getAvailableTruck(weight));
    }

    /**
     * Makes the truck unavailable because it is currently in use
     *
     * @param truckPlateNumber - Unique id for truck
     */
    public void depositeTruck(String truckPlateNumber) throws Exception {
        mapper.updateTruck(truckPlateNumber, true);
    }
}