package by.litvin.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

import by.litvin.model.converter.LinkConverter;

@Entity(tableName = "character_table")
public class Character implements Parcelable {

    protected Character(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        thumbnail = in.readParcelable(Image.class.getClassLoader());
        links = in.createTypedArrayList(Link.CREATOR);
    }

    public Character() {
    }

    @SerializedName("id")
    @PrimaryKey
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("thumbnail")
    @Embedded
    private Image thumbnail;

    @SerializedName("urls")
    @TypeConverters(LinkConverter.class)
    private List<Link> links;

    @Ignore
    private boolean isFavourite = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Image thumb) {
        this.thumbnail = thumb;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeParcelable(thumbnail, flags);
        dest.writeTypedList(links);
    }

    public static final Creator<Character> CREATOR = new Creator<Character>() {
        @Override
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Character character = (Character) o;
        return Objects.equals(name, character.name) &&
                Objects.equals(description, character.description) &&
                Objects.equals(thumbnail, character.thumbnail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, thumbnail);
    }
}
