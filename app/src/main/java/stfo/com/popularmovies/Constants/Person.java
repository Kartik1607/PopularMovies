package stfo.com.popularmovies.Constants;

/**
 * Created by Kartik Sharma on 30/09/16.
 */
public class Person {
    private  String name, imagePath, birthday, deathday, bio;
    private  int id;

    public Person(int id, String name, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getId() {
        return id;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }

    public String getBio() {
        return bio;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setId(int id) {
        this.id = id;
    }
}
