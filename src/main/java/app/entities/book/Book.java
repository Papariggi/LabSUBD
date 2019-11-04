package app.entities.book;

public class Book {
    private int bookId;
    private String title;
    private int yearOfWriting;
    private int publisher_id;

    public Book(int bookId, String title, int yearOfWriting, int publisher_id) {
        this.bookId = bookId;
        this.title = title;
        this.yearOfWriting = yearOfWriting;
        this.publisher_id = publisher_id;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id=" + bookId +
                ", Title='" + title + '\'' +
                ", yearOfWriting=" + yearOfWriting +
                '}';
    }

    //getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getYearOfWriting() {
        return yearOfWriting;
    }

    public void setYearOfWriting(int yearOfWriting) {
        this.yearOfWriting = yearOfWriting;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }
}
