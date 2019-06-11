package by.litvin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RelatedItem implements Parcelable {

    protected RelatedItem(Parcel in) {
        id = in.readLong();
        title = in.readString();
        thumbnail = in.readParcelable(Image.class.getClassLoader());
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeParcelable(thumbnail, flags);
    }

    public static final Creator<RelatedItem> CREATOR = new Creator<RelatedItem>() {
        @Override
        public RelatedItem createFromParcel(Parcel in) {
            return new RelatedItem(in);
        }

        @Override
        public RelatedItem[] newArray(int size) {
            return new RelatedItem[size];
        }
    };
}
