package se.fearless.spacedweb.persistance.dao;

import se.fearless.common.uuid.UUID;
import se.fearless.spacedweb.persistance.PersistableBase;

import java.util.List;

public interface Dao<T extends PersistableBase> {
    T persist(T obj);

    void delete(T obj);

    T findByPk(UUID key);

    List<T> findAll();

    void deleteAll();

}
