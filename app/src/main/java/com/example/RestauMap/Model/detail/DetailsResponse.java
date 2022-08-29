
package com.example.RestauMap.Model.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailsResponse {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private DetailsData detailsData;

    /**
     * No args constructor for use in serialization
     * 
     */
    public DetailsResponse() {
    }

    /**
     * 
     * @param detailsData
     * @param success
     * @param message
     */
    public DetailsResponse(String success, String message, DetailsData detailsData) {
        super();
        this.success = success;
        this.message = message;
        this.detailsData = detailsData;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DetailsData getDetailsData() {
        return detailsData;
    }

    public void setDetailsData(DetailsData detailsData) {
        this.detailsData = detailsData;
    }

}
