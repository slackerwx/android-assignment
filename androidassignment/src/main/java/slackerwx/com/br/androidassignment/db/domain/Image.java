package slackerwx.com.br.androidassignment.db.domain;

import java.io.Serializable;

/**
 * Created by slackerwx on 06/07/15.
 */
public class Image implements Serializable {


    private String url;
    private Integer width;
    private Integer height;


    public String getUrl() {
        return url;
    }


    public Integer getWidth() {
        return width;
    }


    public Integer getHeight() {
        return height;
    }


}
