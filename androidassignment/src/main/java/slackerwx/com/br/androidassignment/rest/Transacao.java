package slackerwx.com.br.androidassignment.rest;

/**
 * Created by slackerwx on 06/07/15.
 */
public interface Transacao {
    void executar() throws Exception;

    void atualizarView();
}
