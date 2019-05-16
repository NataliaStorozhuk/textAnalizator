package sample.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sample.DBModels.Book;
import sample.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class BookDao {

    public static Book findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Book.class, id);
    }

    public static void save(Book book) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(book);
        tx1.commit();
        session.close();
    }


    public static void update(Book book) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(book);
        tx1.commit();
        session.close();
    }

    public static void delete(Book book) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(book);
        tx1.commit();
        session.close();
    }

    public static List<Book> findAll() {
        List<Book> Books = (List<Book>) HibernateSessionFactoryUtil.getSessionFactory().openSession().
                createQuery("From Book").list();
        return Books;
    }
}
