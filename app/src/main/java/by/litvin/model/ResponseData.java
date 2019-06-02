package by.litvin.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseData<T> {
    @SerializedName("total")
    private int total;

    @SerializedName("count")
    private int count;

    @SerializedName("results")
    private List<T> results;

    public int getCount() {
        return count;
    }

    public int getTotal() {
        return total;
    }

    public List<T> getResults() {
        return results;
    }
}
