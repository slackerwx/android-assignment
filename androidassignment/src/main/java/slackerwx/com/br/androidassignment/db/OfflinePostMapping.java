package slackerwx.com.br.androidassignment.db;

import java.util.List;

import slackerwx.com.br.androidassignment.db.domain.Image;
import slackerwx.com.br.androidassignment.db.domain.Images;
import slackerwx.com.br.androidassignment.db.domain.Location;
import slackerwx.com.br.androidassignment.db.domain.Media;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;

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
        if (location != null) {
            locationName = location.getName();

            Double lat = location.getLatitude();
            Double longi = location.getLongitude();

            final String latitude = lat != null ? Double.toString(lat) : "";
            final String longitude = longi != null ? Double.toString(longi) : "";
            latLng = latitude + " " + longitude;
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
                imageThumbnailUrl, username);
    }
}
