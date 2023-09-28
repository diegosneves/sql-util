package diegosneves.sql.database;

import diegosneves.sql.connection.HibernateConnectionSingleton;
import diegosneves.sql.repository.Repository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Interface com implementacoes para utilizar o SQL como base de dados.<br>
 *
 * @param <T> Entidade que será persistida no banco de dados
 * @param <K> Tipo de dado que será utilizado pelo banco de dados para representar o ID(MySQL usa Long como ID por padrao).
 */
public interface SqlRepository<T, K> extends Repository<T, K> {

    static Session getConnection() {
        return HibernateConnectionSingleton.getSession();
    }

    static void closeConnection(Session session) {
        session.close();
    }

    @Override
    default T findById(K id) {
        Session session = getConnection();
        T obj;
        try {
            obj = session.get(this.getObjType(), (Serializable) id);
        } finally {
            closeConnection(session);
        }
        return obj;
    }

    @Override
    default T save(T data) {
        Session session = getConnection();
        T obj;
        try {
            Serializable id = session.save(data);
            obj = session.get(this.getObjType(), id);
        } finally {
            closeConnection(session);
        }
        return obj;
    }

    @Override
    default T update(T data) {
        Session session = getConnection();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(data);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            closeConnection(session);
        }
        return data;
    }

    @Override
    default void delete(T data) {
        Session session = getConnection();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(data);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            closeConnection(session);
        }
    }

    default T findBy(String atributo, Object valor) {
        Session session = getConnection();
        T obj;
        try {
            Query<T> query = session.createQuery("FROM " + this.getObjType().getSimpleName() + " p WHERE p." + atributo + " = :valor", this.getObjType());
            query.setParameter("valor", valor);
            obj = query.uniqueResult();
        } finally {
            SqlRepository.closeConnection(session);
        }
        return obj;
    }

    @Override
    default List<T> findAll() {
        Session session = getConnection();
        List<T> resultList;
        try {
            Query<T> query = session.createQuery("FROM " + this.getObjType().getSimpleName(), this.getObjType());
            resultList = query.list();
        } finally {
            closeConnection(session);
        }
        return resultList;
    }

}
