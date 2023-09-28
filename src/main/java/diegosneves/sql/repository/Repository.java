package diegosneves.sql.repository;

import java.util.List;

/**
 * Interface padrao para implementacao basica de CRUD
 * @param <T> Entity/Document -  que será persistida.
 * @param <K> Tipo do ID que será utilizado.
 */
public interface Repository<T, K> {

    /**
     * Esse metodo deve ser implementado na interface da entity para que posso ser melhor utilizada.
     * <br><br>Ex.:<br>
     *
     * <p>
     * public interface <b>PessoaRepository</b> extends <b>{@code MongoRepository<Pessoa>}</b>
     * <blockquote><pre>
     *     {@code @Override}
     *     default {@code Class<Pessoa>} getObjType() {
     *         return Pessoa.class;
     *     }
     * </pre></blockquote>
     * <p>
     *
     * @return a Classe da entity implementada
     */
    Class<T> getObjType();

    T save(T data);

    T findById(K id);

    void delete(T data);

    T update(T data);

    List<T> findAll();

}
