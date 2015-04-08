package se.fearless.spacedweb.persistance.hibernate.daoimpl;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.fearless.spacedweb.model.UserAccount;
import se.fearless.spacedweb.persistance.dao.UserAccountDao;

@Repository
public class UserAccountDaoImpl extends DaoImpl<UserAccount> implements UserAccountDao {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public UserAccountDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, UserAccount.class);
    }


    @Override
    public UserAccount findByUsername(String userName) {
        Criteria crit = getSession().createCriteria(UserAccount.class);
        crit.add(Restrictions.eq("username", userName));
        return (UserAccount) crit.uniqueResult();
    }

    @Override
    public UserAccount findByEmail(String email) {
        Criteria crit = getSession().createCriteria(UserAccount.class);
        crit.add(Restrictions.eq("email", email));
        return (UserAccount) crit.uniqueResult();
    }

}
