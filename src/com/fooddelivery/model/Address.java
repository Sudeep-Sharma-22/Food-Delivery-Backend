package com.fooddelivery.model;

public class Address {
    private int addressId;
    private int userId;
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
    private boolean isDefault;
    private String label;

    public Address() {}

    public Address(int addressId, int userId, String addressLine, String city, String state, String pincode, boolean isDefault, String label) {
        this.addressId = addressId;
        this.userId = userId;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.isDefault = isDefault;
        this.label = label;
    }

    public int getAddressId() { return addressId; }
    public void setAddressId(int addressId) { this.addressId = addressId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean aDefault) { isDefault = aDefault; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + addressId +
                ", " + addressLine + ", " + city + ", " + pincode +
                " (" + label + ")}";
    }
}
