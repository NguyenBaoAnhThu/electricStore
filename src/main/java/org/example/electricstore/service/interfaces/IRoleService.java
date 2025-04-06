package org.example.electricstore.service.interfaces;

public interface IRoleService<T>{

    void saveRole(T role);
    void deleteRole(T role);
}
