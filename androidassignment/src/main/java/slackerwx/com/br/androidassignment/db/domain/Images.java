package slackerwx.com.br.androidassignment.db.domain;

/**
 * Created by slackerwx on 06/07/15.
 */
public class Images {

    private Image low_resolution;
    private Image thumbnail;
    private Image standard_resolution;

    public Image getLowResolution() {
        return low_resolution;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public Image getStandardResolution() {
        return standard_resolution;
    }

}
