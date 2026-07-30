package com.fooddelivery.dao;

import com.fooddelivery.model.Address;
import java.util.List;

public interface IAddressDao {
    boolean insertAddress(Address address);
    List<Address> getAddressesByUserId(int userId);
}
