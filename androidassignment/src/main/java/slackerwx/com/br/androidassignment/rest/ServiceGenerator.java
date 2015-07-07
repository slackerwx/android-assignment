package slackerwx.com.br.androidassignment.rest;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by slackerwx on 06/07/15.
 */
public class ServiceGenerator {

    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
