package slackerwx.com.br.androidassignment.activities;

import slackerwx.com.br.androidassignment.db.domain.OfflinePost;

/**
 * Created by slackerwx on 07/07/15.
 */
public class OnMediaClickEvent {

    private OfflinePost post;

    public OnMediaClickEvent(OfflinePost post) {
        this.post = post;
    }

    public OfflinePost getPost() {
        return post;
    }
}
