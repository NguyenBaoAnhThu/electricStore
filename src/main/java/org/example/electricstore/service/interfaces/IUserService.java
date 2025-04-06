package org.example.electricstore.service.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface IUserService<T> {
    T findById(int id);
    T save(T t);
    T update(T t , int id);
    void delete(int id);
}
