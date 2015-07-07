package slackerwx.com.br.androidassignment.rest.endpoints;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import slackerwx.com.br.androidassignment.db.domain.RecentByTag;

/**
 * Created by slackerwx on 07/07/15.
 */
public interface TagEndpoints {

    @GET("/tags/{tag_name}/media/recent")
    RecentByTag getRecent(
            @Path("tag_name") String tagName,
            @Query("client_id") String clientId,
            @Query("min_id") String minId,
            @Query("max_id") String maxId
    );
}
