package slackerwx.com.br.androidassignment.db.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by slackerwx on 07/07/15.
 */
public class OfflinePost {

    @DatabaseField(id = true) private String id;
    @DatabaseField private String imageUrl;
    @DatabaseField private String userFullName;
    @DatabaseField private String userId;
    @DatabaseField private String tags;
    @DatabaseField private String createdTime;
    @DatabaseField private String latLng;
    @DatabaseField private String locationName;
    @DatabaseField private String likesCount;
    @DatabaseField private String imageThumbnailUrl;
    @DatabaseField private String username;

    public OfflinePost(String id, String imageUrl, String userFullName, String userId, String tags, String createdTime, String latLng,
                       String locationName, String likesCount, String imageThumbnailUrl, String userName) {
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

    public String getCreatedTime() {
        return createdTime;
    }

    public String getLatLng() {
        return latLng;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLikesCount() {
        return likesCount;
    }

    public String getImageThumbnailUrl() {
        return imageThumbnailUrl;
    }

    public String getUsername() {
        return username;
    }
}
