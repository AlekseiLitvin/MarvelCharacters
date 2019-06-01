package by.litvin.model;

public class Character {

    private int id;
    private String name;
    private String description;
    private Image thumb;

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

    public Image getThumb() {
        return thumb;
    }

    public void setThumb(Image thumb) {
        this.thumb = thumb;
    }
}
