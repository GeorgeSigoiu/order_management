package model;

/**
 * Reprezinta produsul care poate fi comandat de catre un client
 */
public class Product {
    private int id;
    private String denomination, brand;
    private double price;
    private int quantity;

    public Product(int id, String denomination, String brand, double price, int quantity) {
        this.id = id;
        this.denomination = denomination;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDenomination() {
        return denomination;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
