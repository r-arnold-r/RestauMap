
package com.example.RestauMap.Model.listing;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListingResponse {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ListingData> data = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ListingResponse() {
    }

    /**
     * 
     * @param data
     * @param success
     * @param message
     */
    public ListingResponse(String success, String message, List<ListingData> data) {
        super();
        this.success = success;
        this.message = message;
        this.data = data;
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

    public List<ListingData> getData() {
        return data;
    }

    public void setData(List<ListingData> data) {
        this.data = data;
    }

}
