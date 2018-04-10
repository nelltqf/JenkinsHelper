package happy.rabbit.data;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtil {

    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class);

    private SessionFactory sessionFactory;

    private Session session;

    @Autowired
    public HibernateUtil(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public Session getCurrentSession() {
        try {
            if (session == null) {
                session = this.sessionFactory.openSession();
            }
            return session;
        } catch (HibernateException e) {
            LOGGER.warn("Reopening session", e);
            throw new IllegalStateException();
        }
    }
}
