
package com.example.RestauMap.Model.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterPOST {

    @SerializedName("response")
    @Expose
    private RegisterResponse registerResponse;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RegisterPOST() {
    }

    /**
     * 
     * @param registerResponse
     */
    public RegisterPOST(RegisterResponse registerResponse) {
        super();
        this.registerResponse = registerResponse;
    }

    public RegisterResponse getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(RegisterResponse registerResponse) {
        this.registerResponse = registerResponse;
    }

}
