package com.example.parkmania.models;

import java.util.List;

public class Users {

    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String plateNumber;
    private List<Parking> parkings;

    public Users(String name, String email, String password, String phone, String plateNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.plateNumber = plateNumber;
    }
    public Users() {

    }

    public void addParking(Parking parking) {
        parkings.add(parking);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                '}';
    }
}
