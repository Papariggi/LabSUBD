package app.entities.book;

public class FullBook {
    private String title;
    private String author;
    private int yearOfWriting;
    private String publisherName;

    public FullBook(String title, String author, int yearOfWriting, String publisherName) {
        this.title = title;
        this.author = author;
        this.yearOfWriting = yearOfWriting;
        this.publisherName = publisherName;
    }

    @Override
    public String toString() {
        return "FullBook{" +
                "Title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", yearOfWriting=" + yearOfWriting +
                ", publisher='" + publisherName + '\'' + "}";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfWriting() {
        return yearOfWriting;
    }

    public void setYearOfWriting(int yearOfWriting) {
        this.yearOfWriting = yearOfWriting;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
}
