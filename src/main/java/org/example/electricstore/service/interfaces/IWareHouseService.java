package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.WareHouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface IWareHouseService<T> {
   Page<T> searchWareHouses(LocalDate importDate, String brand, Integer statusStock,
                            String productCode, String productName, Pageable pageable);
   List<WareHouse> getWareHouses();
}
