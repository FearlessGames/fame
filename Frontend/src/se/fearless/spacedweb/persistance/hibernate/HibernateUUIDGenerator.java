package se.fearless.spacedweb.persistance.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;
import se.fearlessgames.common.util.SystemTimeProvider;
import se.fearlessgames.common.util.uuid.UUIDFactory;
import se.fearlessgames.common.util.uuid.UUIDFactoryImpl;

import java.io.Serializable;
import java.security.SecureRandom;

public class HibernateUUIDGenerator implements IdentifierGenerator {
    private final UUIDFactory uuidFactory = new UUIDFactoryImpl(new SystemTimeProvider(), new SecureRandom());

    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        return uuidFactory.combUUID();
    }
}
