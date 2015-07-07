package slackerwx.com.br.androidassignment.db.config;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import slackerwx.com.br.androidassignment.config.MyApplication;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;


/**
 * Classe responsável por criar os DAO's responsáveis por fazerem
 * o acesso ao banco de dados SQLite.
 * <p/>
 * Created by slackerwx on 16/04/15.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    public static final String DATABASE_NAME = "android_assignment.db";

    // any time you make changes to your database objects, you may have to increase the database version
    public static int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<OfflinePost, Long> mDomainDAO = null;

    public DatabaseHelper() {
        super(MyApplication.getAppContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }


    /**
     * This is called when the database is first created. Usually you should
     * call createTable statements here to create the tables that will store
     * your data.
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            TableUtils.createTable(connectionSource, OfflinePost.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher
     * version number. This allows you to adjust the various data to match the
     * new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");

            if (oldVersion == -1 && newVersion == -1) {
                TableUtils.dropTable(connectionSource, OfflinePost.class, true);

                // after we drop the old databases, we create the new ones
                onCreate(database, connectionSource);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    public RuntimeExceptionDao<OfflinePost, Long> getDomainDAO() {
        if (mDomainDAO == null) {
            mDomainDAO = getRuntimeExceptionDao(OfflinePost.class);
        }
        return mDomainDAO;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        mDomainDAO = null;
    }


}