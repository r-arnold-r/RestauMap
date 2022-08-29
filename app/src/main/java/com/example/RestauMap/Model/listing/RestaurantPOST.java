
package com.example.RestauMap.Model.listing;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantPOST {

    @SerializedName("response")
    @Expose
    private ListingResponse listingResponse;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RestaurantPOST() {
    }

    /**
     * 
     * @param listingResponse
     */
    public RestaurantPOST(ListingResponse listingResponse) {
        super();
        this.listingResponse = listingResponse;
    }

    public ListingResponse getListingResponse() {
        return listingResponse;
    }

    public void setListingResponse(ListingResponse listingResponse) {
        this.listingResponse = listingResponse;
    }

}
