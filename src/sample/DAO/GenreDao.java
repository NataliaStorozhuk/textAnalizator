package sample.DAO;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sample.DBModels.Book;
import sample.DBModels.Genre;
import sample.utils.HibernateSessionFactoryUtil;

import java.util.List;

public class GenreDao {

    public static Genre findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Genre.class, id);
    }

    public static void save(Genre Genre) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(Genre);
        tx1.commit();
        session.close();
    }


    public static void update(Genre Genre) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(Genre);
        tx1.commit();
        session.close();
    }

    public static void delete(Genre Genre) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(Genre);
        tx1.commit();
        session.close();
    }

    public static Book findBookById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(Book.class, id);
    }

    public static List<Genre> findAll() {
        List<Genre> Genres = (List<Genre>) HibernateSessionFactoryUtil.getSessionFactory().openSession().
                createQuery("From Genre").list();
        return Genres;
    }
}
