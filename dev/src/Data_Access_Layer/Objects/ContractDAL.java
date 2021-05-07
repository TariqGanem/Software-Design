package Data_Access_Layer.Objects;

import DTO.ContractDTO;
import DTO.ItemDTO;

import java.util.HashMap;
import java.util.Map;

public class ContractDAL {
    private Map<Integer, ItemDAL> items;
    private QuantityReportDAL report;
    private boolean selfPickup;

    public ContractDAL(Map<Integer, ItemDAL> items, QuantityReportDAL report, boolean selfPickup) {
        this.items = items;
        this.report = report;
        this.selfPickup = selfPickup;
    }

    public ContractDTO DTO(){
        Map<Integer, ItemDTO> itemsDTO = new HashMap<>();
        for (Integer item : items.keySet()) {
            itemsDTO.put(item,items.get(item).DTO());
        }
        return new ContractDTO(itemsDTO,report.DTO(),selfPickup);
    }

    public void addDiscount(int itemid, int quant, double disc) {
        if(items.containsKey(itemid)) {
            report.addDiscount(itemid, quant, disc);
        }
    }

    public void addItem(int itemid, String name, double price) {
        if(!items.containsKey(itemid)){
            items.put(itemid,new ItemDAL(itemid,name,price));
        }
    }

    public void removeDiscount(int itemid, int quant) {
        if(items.containsKey(itemid)){
            report.removeDiscount(itemid,quant);
        }
    }

    public void removeItem(int itemid) {
        if(items.containsKey(itemid)){
            items.remove(itemid);
            report.removeDiscount(itemid);
        }
    }

    public void updatePrice(int itemid, double price) {
        if(items.containsKey(itemid)){
            items.get(itemid).updatePrice(price);
        }
    }
}
