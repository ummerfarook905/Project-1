package com.example.aquafin.Dto;

public class ProductDto {
    private String name;
    private String description;
    private String price;
    private String quantity;  

    public String getProductName() {
        return name;
    }

    public void setProductName(String ProductName) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String Price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String Quantity) {
        this.quantity = quantity;
    }

}
