package slackerwx.com.br.androidassignment.db.bo;

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

/**
 * Interface p/ operações com o SQLite
 * <p/>
 * Created by slackerwx on 16/04/15.
 */
public interface Crud<T> {

    int criar(T object);

    Dao.CreateOrUpdateStatus criarOuAtualizar(T object);

    T buscar(Long idSqlite);

    ArrayList<T> buscarTodos();

    int atualizar(T object);

    int excluir(T object);
}
