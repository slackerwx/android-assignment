package slackerwx.com.br.androidassignment.db;

import java.util.List;

import slackerwx.com.br.androidassignment.Constants;
import slackerwx.com.br.androidassignment.db.domain.OfflinePost;
import slackerwx.com.br.androidassignment.rest.model.Image;
import slackerwx.com.br.androidassignment.rest.model.Images;
import slackerwx.com.br.androidassignment.rest.model.Location;
import slackerwx.com.br.androidassignment.rest.model.Media;
import slackerwx.com.br.androidassignment.utils.SharedPreferencesUtils;
import slackerwx.com.br.androidassignment.utils.Utils;

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

        Location location = media.getLocation();

        if (location != null) {

            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            if (latitude != null && longitude != null) {
                double distanciaMaxima = getDistanciaMaxima(latitude, longitude);

                if (distanciaMaxima < Constants.DISTANCIA_MAXIMA) {

                    final String imageUrl = getStandardResolutionImgUrl(media);
                    final String locationName = location.getName();
                    final String latLng = Double.toString(latitude) + " " + Double.toString(longitude);
                    final String userFullName = media.getUser().getUsername();
                    final String userId = media.getUser().getId();
                    final String allTags = preencherTags(media);
                    final int likesCount = media.getLikes().getCount();
                    final long createdTime = media.getCreatedTime();
                    final String imageThumbnailUrl = media.getImages().getThumbnail().getUrl();
                    final String username = media.getUser().getUsername();
                    final String id = media.getId();

                    return new OfflinePost(id, imageUrl, userFullName, userId, allTags, createdTime, latLng, locationName, likesCount,
                            imageThumbnailUrl, username, latitude, longitude);

                }

            }

        }

        return null;

    }

    private String getStandardResolutionImgUrl(Media media) {
        Images images = media.getImages();
        Image standardResolution = images.getStandardResolution();
        return standardResolution.getUrl();
    }

    private String preencherTags(Media media) {
        List<String> tags = media.getTags();

        StringBuilder sbTags = new StringBuilder();
        for (String tag : tags) {
            sbTags.append("#");
            sbTags.append(tag);
            sbTags.append(" ");
        }
        return sbTags.toString();
    }

    private double getDistanciaMaxima(double latitude, double longitude) {
        String latLngAtual = SharedPreferencesUtils.getLatLngAtual();

        String[] split = latLngAtual.split(" ");
        double mLatAtual = Double.valueOf(split[0]);
        double mLongAtual = Double.valueOf(split[1]);


        final double distancia = Utils.getDistanceFromLocation(mLatAtual, mLongAtual, latitude, longitude, 'K');

        return distancia;
    }
}
