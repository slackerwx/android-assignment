package slackerwx.com.br.androidassignment.db.domain;

import com.j256.ormlite.field.DatabaseField;

import java.util.Comparator;

import slackerwx.com.br.androidassignment.utils.SharedPreferencesUtils;
import slackerwx.com.br.androidassignment.utils.Utils;

/**
 * Created by slackerwx on 07/07/15.
 */
public class OfflinePost {

    @DatabaseField(id = true) private String id;
    @DatabaseField private String imageUrl;
    @DatabaseField private String userFullName;
    @DatabaseField private String userId;
    @DatabaseField private String tags;
    @DatabaseField private long createdTime;
    public static final Comparator<OfflinePost> DATE_ASC = new Comparator<OfflinePost>() {
        @Override
        public int compare(OfflinePost lhs, OfflinePost rhs) {
            long x = lhs.getCreatedTime();
            long y = rhs.getCreatedTime();
            return y < x ? -1
                    : y > x ? 1
                    : 0;
        }
    };
    @DatabaseField private String latLng;
    @DatabaseField private String locationName;
    @DatabaseField private int likesCount;
    public static final Comparator<OfflinePost> LIKES_ASC = new Comparator<OfflinePost>() {
        @Override
        public int compare(OfflinePost lhs, OfflinePost rhs) {
            int x = lhs.getLikesCount();
            int y = rhs.getLikesCount();
            return y < x ? -1
                    : y > x ? 1
                    : 0;
        }
    };
    @DatabaseField private String imageThumbnailUrl;
    @DatabaseField private String username;
    @DatabaseField private double latitude;
    @DatabaseField private double longitude;
    public static final Comparator<OfflinePost> DISTANCE_DESC = new Comparator<OfflinePost>() {

        @Override
        public int compare(OfflinePost lhs, OfflinePost rhs) {
            double mLhsMediaLocationLat = lhs.getLatitude();
            double mLhsMediaLocationLong = lhs.getLongitude();

            double mRhsMediaLocationLat = rhs.getLatitude();
            double mRhsMediaLocationLong = rhs.getLongitude();

            String latLngAtual = SharedPreferencesUtils.getLatLngAtual();

            String[] split = latLngAtual.split(" ");
            double mLatAtual = Double.valueOf(split[0]);
            double mLongAtual = Double.valueOf(split[1]);


            double x = Utils.getDistanceFromLocation(mLatAtual, mLongAtual, mLhsMediaLocationLat, mLhsMediaLocationLong, 'K');
            double y = Utils.getDistanceFromLocation(mLatAtual, mLongAtual, mRhsMediaLocationLat, mRhsMediaLocationLong, 'K');
            return x < y ? -1
                    : x > y ? 1
                    : 0;
        }
    };

    public OfflinePost(String id, String imageUrl, String userFullName, String userId, String tags, long createdTime, String latLng,
                       String locationName, int likesCount, String imageThumbnailUrl, String userName, double latitude, double longitude) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.userFullName = userFullName;
        this.userId = userId;
        this.tags = tags;
        this.createdTime = createdTime;
        this.latLng = latLng;
        this.locationName = locationName;
        this.likesCount = likesCount;
        this.imageThumbnailUrl = imageThumbnailUrl;
        this.username = userName;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public OfflinePost() {

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public String getUserId() {
        return userId;
    }

    public String getTags() {
        return tags;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public String getLatLng() {
        return latLng;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public String getUsername() {
        return username;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


}
