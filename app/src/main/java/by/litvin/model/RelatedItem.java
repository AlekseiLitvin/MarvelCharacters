package by.litvin.model;

import com.google.gson.annotations.SerializedName;

public class RelatedItem {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("thumbnail")
    private Image thumbnail;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Image getThumbnail() {
        return thumbnail;
    }
}
