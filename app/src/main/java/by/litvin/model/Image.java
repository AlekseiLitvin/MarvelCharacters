package by.litvin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Image implements Parcelable {

    @Ignore
    protected Image(Parcel in) {
        path = in.readString();
        extension = in.readString();
    }

    @Ignore
    public Image(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public Image() {
    }

    @SerializedName("path")
    private String path;

    @SerializedName("extension")
    private String extension;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(extension);
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(path, image.path) &&
                Objects.equals(extension, image.extension);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, extension);
    }
}
