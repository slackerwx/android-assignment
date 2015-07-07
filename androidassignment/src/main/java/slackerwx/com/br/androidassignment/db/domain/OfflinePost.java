package slackerwx.com.br.androidassignment.db.domain;

import com.j256.ormlite.field.DatabaseField;

import java.util.Comparator;

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

    public OfflinePost(String id, String imageUrl, String userFullName, String userId, String tags, long createdTime, String latLng,
                       String locationName, int likesCount, String imageThumbnailUrl, String userName) {
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
}
