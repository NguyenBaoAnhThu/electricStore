package org.example.electricstore.service.interfaces;


import java.util.List;


public interface IEmployeePositionService<T>{
    List<T> getEmployeePositions();
    void deleteById(Integer id);
}
