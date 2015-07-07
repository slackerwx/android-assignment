package slackerwx.com.br.androidassignment.db.bo;

/**
 * Created by jake on 07/07/15.
 */

import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

import slackerwx.com.br.androidassignment.db.domain.OfflinePost;


/**
 * Created by slackerwx on 16/04/15.
 */
public class OfflinePostBo extends BaseBo<OfflinePost> implements Crud<OfflinePost> {

    private static OfflinePostBo mDomainBO;

    private OfflinePostBo() {
        super();
        classeDao = databaseHelper.getDomainDAO();
    }

    public static OfflinePostBo getInstance() {
        if (mDomainBO == null) {
            mDomainBO = new OfflinePostBo();
        }

        return mDomainBO;
    }

    @Override
    public int criar(OfflinePost domainClass) {
        return create(domainClass);
    }

    @Override
    public Dao.CreateOrUpdateStatus criarOuAtualizar(OfflinePost domainClass) {
        return createOrUpdate(domainClass);
    }

    @Override
    public OfflinePost buscar(Long idSqlite) {
        return retrieve(idSqlite);
    }

    @Override
    public ArrayList<OfflinePost> buscarTodos() {
        return (ArrayList<OfflinePost>) retrieveAll();
    }

    @Override
    public int atualizar(OfflinePost domainClass) {
        return update(domainClass);
    }

    @Override
    public int excluir(OfflinePost domainClass) {
        return delete(domainClass);
    }
}
