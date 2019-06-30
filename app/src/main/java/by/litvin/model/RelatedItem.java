package by.litvin.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

    @BindingAdapter("relatedItemImage")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .apply(new RequestOptions().fitCenter())
                .into(imageView);
    }

    @BindingAdapter("bigRelatedItemImage")
    public static void loadBigImage(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);

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
