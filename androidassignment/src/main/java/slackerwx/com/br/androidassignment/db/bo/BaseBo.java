package slackerwx.com.br.androidassignment.db.bo;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import slackerwx.com.br.androidassignment.db.config.DatabaseHelper;

/**
 * Created by slackerwx on 16/04/15.
 */
public abstract class BaseBo<T> {
    private final String LOG_TAG = getClass().getSimpleName();

    protected DatabaseHelper databaseHelper;

    protected RuntimeExceptionDao<T, Long> classeDao;


    public BaseBo() {
        this.databaseHelper = new DatabaseHelper();
    }

    public int create(T object) {
        return classeDao.create(object);
    }

    public Dao.CreateOrUpdateStatus createOrUpdate(T object) {
        return classeDao.createOrUpdate(object);
    }

    public T retrieve(Long idSqlite) {
        return classeDao.queryForId(idSqlite);
    }

    public List<T> retrieveAll() {
        return classeDao.queryForAll();
    }

    public int update(T object) {
        return classeDao.update(object);
    }

    public int delete(T object) {
        return classeDao.delete(object);
    }


}
