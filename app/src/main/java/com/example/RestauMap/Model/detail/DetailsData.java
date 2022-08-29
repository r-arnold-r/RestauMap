
package com.example.RestauMap.Model.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsData {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    /**
     * No args constructor for use in serialization
     *
     * @param id
     * @param name
     * @param description
     * @param img_temp
     * @param rating
     * @param latitude
     * @param longitude
     */

    /**
     * 
     * @param img
     * @param latitude
     * @param name
     * @param rating
     * @param description
     * @param id
     * @param longitude
     */
    public DetailsData(String id, String name, String description, String img, String rating, String latitude, String longitude) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.img = img;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
