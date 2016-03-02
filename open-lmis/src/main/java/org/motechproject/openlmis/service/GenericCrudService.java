package org.motechproject.openlmis.service;

import java.util.List;

public interface GenericCrudService<T> {
    List<T> findAll();
    void update(T dataElement);
    void delete(T dataElement);
    void deleteAll();
}
