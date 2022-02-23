package model;

/**
 * Reprezinta o comanda efectuata de catre un client
 */
public class Order {
    private int id;
    private String productName, clientName;
    private double pricePerItem;
    private int productQuantity;
    private double totalPrice;

    public Order(int id, String clientName, String productName, double pricePerItem, int productQuantity, double totalPrice) {
        this.id = id;
        this.clientName = clientName;
        this.productName = productName;
        this.pricePerItem = pricePerItem;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPricePerItem() {
        return pricePerItem;
    }

    public void setPricePerItem(double pricePerItem) {
        this.pricePerItem = pricePerItem;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
