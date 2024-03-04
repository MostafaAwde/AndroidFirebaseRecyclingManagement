package com.example.recycling;

public class Material {
    private String quantity;

    private String type;
    private String uid;

    public Material() {
    }

    public Material(String quantity, String type, String uid) {
        this.quantity = quantity;
        this.type = type;
        this.uid = uid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return  "quantity= " + quantity +
                "\ntype= " + type;
    }


}
