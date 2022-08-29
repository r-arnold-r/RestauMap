
package com.example.RestauMap.Model.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantDetailPOST {

    @SerializedName("response")
    @Expose
    private DetailsResponse detailsResponse;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RestaurantDetailPOST() {
    }

    /**
     * 
     * @param detailsResponse
     */
    public RestaurantDetailPOST(DetailsResponse detailsResponse) {
        super();
        this.detailsResponse = detailsResponse;
    }

    public DetailsResponse getDetailsResponse() {
        return detailsResponse;
    }

    public void setDetailsResponse(DetailsResponse detailsResponse) {
        this.detailsResponse = detailsResponse;
    }

}
