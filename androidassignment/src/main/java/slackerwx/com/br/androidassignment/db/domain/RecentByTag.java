package slackerwx.com.br.androidassignment.db.domain;

import java.util.List;

import slackerwx.com.br.androidassignment.rest.Pagination;

/**
 * Created by slackerwx on 07/07/15.
 */
public class RecentByTag {


    private Pagination pagination;
    private List<Media> data;


    public Pagination getPagination() {
        return pagination;
    }


    public List<Media> getMediaList() {
        return data;
    }


}