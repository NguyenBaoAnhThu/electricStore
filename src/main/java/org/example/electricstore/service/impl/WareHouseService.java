package org.example.electricstore.service.impl;

import org.example.electricstore.model.WareHouse;
import org.example.electricstore.repository.IWareHouseRepository;
import org.example.electricstore.service.interfaces.IWareHouseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WareHouseService implements IWareHouseService <WareHouse> {
    private final IWareHouseRepository wareHouseRepository;
    public WareHouseService(IWareHouseRepository wareHouseRepository) {
        this.wareHouseRepository = wareHouseRepository;
    }

    @Override
    public Page<WareHouse> searchWareHouses(String field, String keyword, Integer statusStock,int page , int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return this.wareHouseRepository.searchWareHouse(field, keyword, statusStock, pageable);
    }

    @Override
    public List<WareHouse> getWareHouses() {
        return this.wareHouseRepository.findAll();
    }
}

