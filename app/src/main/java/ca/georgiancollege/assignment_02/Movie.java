package ca.georgiancollege.assignment_02;

public class Movie {

    private String id;
    private String title;
    private String director;
    private String year;
    private String userId;

    public Movie() {
    }

    public Movie(String title, String director, String year, String userId) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }
    public void setDirector(String director) {
        this.director = director;
    }

    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
}