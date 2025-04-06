package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.WareHouse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IWareHouseService<T> {
   Page<T> searchWareHouses(String field , String keyword , Integer statusStock ,int page , int size);
   List<WareHouse> getWareHouses();
}
