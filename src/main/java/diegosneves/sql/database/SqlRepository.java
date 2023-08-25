package diegosneves.sql.database;

import diegosneves.sql.connection.HibernateConnectionSingleton;
import diegosneves.sql.repository.Repository;
import org.hibernate.Session;

import java.io.Serializable;

/**
 * Interface com implementacoes para utilizar o SQL como base de dados.<br>
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
    default void delete(T data) {
        Session session = getConnection();
        try {
            session.delete(data);
        } finally {
            closeConnection(session);
        }
    }
}
