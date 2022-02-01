package com.appsdeveloperblog.app.ws.mobileappws.service;

import com.appsdeveloperblog.app.ws.mobileappws.shared.dto.AddressDTO;

import java.util.List;

public interface AddressService {
    List<AddressDTO> getAddressesByUserId(String id);

    AddressDTO getAddressByAddressId(String addressId);
}
