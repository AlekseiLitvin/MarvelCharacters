package by.litvin.model.converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import by.litvin.model.Link;

public class LinkConverter {
    @TypeConverter
    public static List<Link> fromString(String links) {
        Type listType = new TypeToken<List<Link>>() {
        }.getType();
        return new Gson().fromJson(links, listType);
    }

    @TypeConverter
    public static String toString(List<Link> links) {
        return new Gson().toJson(links);
    }
}
