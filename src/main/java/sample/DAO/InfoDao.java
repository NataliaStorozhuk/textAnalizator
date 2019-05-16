package sample.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sample.DBModels.Info;
import sample.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class InfoDao {

    public static Info findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Info.class, id);
    }

    public static void save(Info info) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(info);
        tx1.commit();
        session.close();
    }


    public static void update(Info info) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(info);
        tx1.commit();
        session.close();
    }

    public static void delete(Info info) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(info);
        tx1.commit();
        session.close();
    }

    public static List<Info> findAll() {
        List<Info> Infos = (List<Info>) HibernateSessionFactoryUtil.getSessionFactory().openSession().
                createQuery("From Info").list();
        return Infos;
    }
}
