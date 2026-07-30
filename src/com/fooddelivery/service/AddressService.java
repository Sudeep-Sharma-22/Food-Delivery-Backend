package com.fooddelivery.service;

import com.fooddelivery.dao.IAddressDao;
import com.fooddelivery.dao.AddressDaoImpl;
import com.fooddelivery.model.Address;
import java.util.List;

public class AddressService {
    private IAddressDao addressDao;

    public AddressService() {
        this.addressDao = new AddressDaoImpl();
    }

    public boolean addAddress(int userId, String addressLine, String city, String state, String pincode, String label) {
        if (addressLine == null || city == null || pincode == null || addressLine.trim().isEmpty()) {
            System.err.println("Validation Error: Address fields cannot be empty.");
            return false;
        }

        Address addr = new Address();
        addr.setUserId(userId);
        addr.setAddressLine(addressLine);
        addr.setCity(city);
        addr.setState(state);
        addr.setPincode(pincode);
        addr.setDefault(false);
        addr.setLabel(label != null ? label.toUpperCase() : "HOME");

        return addressDao.insertAddress(addr);
    }

    public List<Address> getUserAddresses(int userId) {
        return addressDao.getAddressesByUserId(userId);
    }
}
