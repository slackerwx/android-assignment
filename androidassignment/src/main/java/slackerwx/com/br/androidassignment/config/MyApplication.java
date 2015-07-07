package slackerwx.com.br.androidassignment.config;

import android.app.Application;
import android.content.ContextWrapper;

/**
 * Classe que armazena o contexto da aplicação para ser usada em várias activity's
 * e fragments.
 * <p/>
 * Created by slackerwx on 16/04/15.
 */
public class MyApplication extends Application {
    private static ContextWrapper appContext;

    public static ContextWrapper getAppContext() {
        return appContext;
    }

    public static void setAppContext(ContextWrapper appContext) {
        MyApplication.appContext = appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MyApplication.setAppContext((ContextWrapper) getApplicationContext());
    }
}