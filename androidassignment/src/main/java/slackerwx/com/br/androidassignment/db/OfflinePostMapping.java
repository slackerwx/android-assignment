package slackerwx.com.br.androidassignment.db;

import java.util.List;

import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.rest.model.Image;
import slackerwx.com.br.androidassignment.rest.model.Images;
import slackerwx.com.br.androidassignment.rest.model.Location;
import slackerwx.com.br.androidassignment.rest.model.Media;

/**
 * Created by jake on 07/07/15.
 */
public class OfflinePostMapping {

    private static OfflinePostMapping INSTANCE;

    public static OfflinePostMapping getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OfflinePostMapping();
        }

        return INSTANCE;
    }

    public OfflinePost getOfflinePost(Media media) {
        Images images = media.getImages();
        Image standardResolution = images.getStandardResolution();
        final String imageUrl = standardResolution.getUrl();


        Location location = media.getLocation();

        String latLng = "";
        String locationName = "";
        double latitude = 0;
        double longitude = 0;
        if (location != null) {
            locationName = location.getName();

            Double lat = location.getLatitude();
            Double longi = location.getLongitude();

            latitude = lat != null ? lat : 0;
            longitude = longi != null ? longi : 0;

            latLng = Double.toString(latitude) + " " + Double.toString(longitude);
        }


        List<String> tags = media.getTags();

        StringBuilder sb = new StringBuilder();
        for (String tag : tags) {
            sb.append("#");
            sb.append(tag);
            sb.append(" ");
        }

        final String userFullName = media.getUser().getUsername();
        final String userId = media.getUser().getId();
        final String allTags = sb.toString();
        final int likesCount = media.getLikes().getCount();
        final long createdTime = media.getCreatedTime();
        final String imageThumbnailUrl = media.getImages().getThumbnail().getUrl();
        final String username = media.getUser().getUsername();
        final String id = media.getId();


        return new OfflinePost(id, imageUrl, userFullName, userId, allTags, createdTime, latLng, locationName, likesCount,
                imageThumbnailUrl, username, latitude, longitude);
    }
}
