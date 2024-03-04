package com.example.recycling;

public class Request {
    private String city;
    private String client_first_name;
    private String client_id;
    private String client_last_name;
    private String material_type;
    private String quantity_requested;
    private String request_id;

    public Request() {

    }

    public Request(String city, String client_first_name, String client_id, String client_last_name, String material_type, String quantity_requested, String request_id) {
        this.city = city;
        this.client_first_name = client_first_name;
        this.client_id = client_id;
        this.client_last_name = client_last_name;
        this.material_type = material_type;
        this.quantity_requested = quantity_requested;
        this.request_id = request_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClient_first_name() {
        return client_first_name;
    }

    public void setClient_first_name(String client_first_name) {
        this.client_first_name = client_first_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_last_name() {
        return client_last_name;
    }

    public void setClient_last_name(String client_last_name) {
        this.client_last_name = client_last_name;
    }

    public String getMaterial_type() {
        return material_type;
    }

    public void setMaterial_type(String material_type) {
        this.material_type = material_type;
    }

    public String getQuantity_requested() {
        return quantity_requested;
    }

    public void setQuantity_requested(String quantity_requested) {
        this.quantity_requested = quantity_requested;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    @Override
    public String toString() {
        return  "city: " + city +
                "\nclient_first_name: " + client_first_name +
                "\nclient_id: " + client_id +
                "\nclient_last_name: " + client_last_name +
                "\nmaterial_type: " + material_type +
                "\nquantity_requested: " + quantity_requested +
                "\nrequest_id: " + request_id;
    }
}
