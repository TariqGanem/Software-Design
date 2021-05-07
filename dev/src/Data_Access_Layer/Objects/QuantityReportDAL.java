package Data_Access_Layer.Objects;


import DTO.QuantityReportDTO;

import java.util.HashMap;
import java.util.Map;

public class QuantityReportDAL {
    private final Map<Integer, Map<Integer, Double>> discounts;

    public QuantityReportDAL() {
        this.discounts = new HashMap<>();
    }

    public QuantityReportDAL(Map<Integer, Map<Integer, Double>> discounts) {
        this.discounts = discounts;
    }

    public QuantityReportDTO DTO(){
        return new QuantityReportDTO(discounts);
    }

    public void addDiscount(int itemid, int quant, double disc) {
        if(discounts.containsKey(itemid)){
            if(!discounts.get(itemid).containsKey(quant)){
                discounts.get(itemid).put(quant,disc);
            }
        }else{
            discounts.put(itemid,new HashMap<>());
            discounts.get(itemid).put(quant,disc);
        }
    }

    public void removeDiscount(int itemid, int quant) {
        if(discounts.containsKey(itemid)){
            if(discounts.get(itemid).containsKey(quant)){
                discounts.get(itemid).remove(quant);
            }
        }
    }

    public void removeDiscount(int itemid) {
        if(discounts.containsKey(itemid)){
            discounts.remove(itemid);
        }
    }
}
