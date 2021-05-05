package BusinessLayer.ShipmentsModule.Objects;

public class Item {

    private int documentId;
    private String name;
    private double amount;
    private double weight;

    public Item(int documentId, String name, double amount, double weight) {
        this.documentId = documentId;
        this.name = name;
        this.amount = amount;
        this.weight = weight;
    }

    public int getDocumentId() {
        return documentId;
    }

    public String getName() {
        return name;
    }


    public double getAmount() {
        return amount;
    }


    public double getWeight() {
        return weight;
    }
}