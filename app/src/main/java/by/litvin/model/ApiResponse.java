package by.litvin.model;

import com.google.gson.annotations.SerializedName;

public class ApiResponse<T> {
    @SerializedName("code")
    private int code;

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private ResponseData<T> data;

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public ResponseData<T> getData() {
        return data;
    }
}
