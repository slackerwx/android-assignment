package slackerwx.com.br.androidassignment.rest.model;

import java.util.List;

/**
 * Created by slackerwx on 07/07/15.
 */
public class Likes {


    private Integer count;
    private List<User> data;


    public Integer getCount() {
        return count;
    }


    public List<User> getUsers() {
        return data;
    }


}
