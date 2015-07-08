package slackerwx.com.br.androidassignment.rest.model;

/**
 * Created by slackerwx on 07/07/15.
 */

import java.util.List;


public class Media {

    private List<String> tags;
    private Location location;
    private Images images;
    private String id;
    private User user;
    private Likes likes;
    private Long created_time;


    public List<String> getTags() {
        return tags;
    }

    public Location getLocation() {
        return location;
    }

    public Likes getLikes() {
        return likes;
    }

    public Images getImages() {
        return images;
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Long getCreatedTime() {
        return created_time;
    }

}
