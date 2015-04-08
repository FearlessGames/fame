package se.fearless.spacedweb.persistance.hibernate.daoimpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import se.fearless.spacedweb.persistance.PersistableBase;
import se.fearless.spacedweb.persistance.dao.Dao;
import se.fearlessgames.common.util.uuid.UUID;

import java.util.List;

public class DaoImpl<T extends PersistableBase> implements Dao<T> {
    private final SessionFactory sessionFactory;
    protected final Class<T> clazz;

    public DaoImpl(SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    @Override
    public T persist(T obj) {
        getSession().saveOrUpdate(obj);
        return obj;
    }

    @Override
    public void delete(T obj) {
        getSession().delete(obj);
    }


    @Override
    public void deleteAll() {
        List<T> list = findAll();
        for (T t : list) {
            delete(t);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T findByPk(UUID key) {
        return (T) getSession().get(clazz, key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findAll() {
        return getSession().createQuery("from " + clazz.getName()).list();
    }

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
