package app.entities.book;

import connection.GetJDBCConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookDatabase {
    private static List<Book> books = new ArrayList<>();
    private static List<Author> authors = new ArrayList<>();
    private static List<Publisher> publishers = new ArrayList<>();
    private static volatile Connection connection;
    private static String URL = "jdbc:mysql://localhost/test";
    private static String USERNAME = "root";
    private static String PASSWORD = "8891";
    private static List<FullBook> fullBooks = new ArrayList<>();
    private int index;

    public BookDatabase(List<Book> books, List<Author> authors, List<Publisher> publishers)
            throws SQLException {
        this.books = books;
        this.publishers = publishers;
        this.authors = authors;
        connection = GetJDBCConnection.init();

        try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            createBooksTable();
            createAuthorTable();
            createPublisherTable();
            createAuthorBookTable();
            createFullBookTable();
        } finally {
            GetJDBCConnection.close();
        }
    }

    public BookDatabase() {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            connection = GetJDBCConnection.init();
            createPublisherTable();
            createBooksTable();
            createAuthorTable();
            createAuthorBookTable();
            createFullBookTable();
            fullBooks = getFullBooks();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int execUpdate(String updateQuery) throws SQLException {
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();
            int result = statement.executeUpdate(updateQuery);
            return result;
        }
    }

    private void createPublisherTable() throws SQLException {
        connection.setAutoCommit(false);
        String authorTableQuery = "CREATE TABLE IF NOT EXISTS publishers " +
                "(publisher_id INTEGER NOT NULL PRIMARY KEY, name TEXT)";
        execUpdate(authorTableQuery);
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void createBooksTable() throws SQLException {
        connection.setAutoCommit(false);
        String booksTableQuery = "CREATE TABLE IF NOT EXISTS books " +
                "(book_id INTEGER NOT NULL PRIMARY KEY, title TEXT NOT NULL," +
                "yearOfWriting INTEGER, publisher_id INTEGER NOT NULL, " +
                "FOREIGN KEY (publisher_id) REFERENCES publishers(publisher_id))";
        execUpdate(booksTableQuery);
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void createAuthorTable() throws SQLException {
        connection.setAutoCommit(false);
        String authorTableQuery = "CREATE TABLE IF NOT EXISTS authors " +
                "(author_id INTEGER NOT NULL PRIMARY KEY, name TEXT)";
        execUpdate(authorTableQuery);
        connection.commit();
        connection.setAutoCommit(true);
    }

    private void createAuthorBookTable() throws SQLException {
        connection.setAutoCommit(false);
        String authorBookTableQuery = "CREATE TABLE IF NOT EXISTS book_author " +
                "(book_id INTEGER NOT NULL, author_id INTEGER NOT NULL, " +
                "PRIMARY KEY (book_id, author_id), " +
                "FOREIGN KEY (book_id) REFERENCES books(book_id), " +
                "FOREIGN KEY (author_id ) REFERENCES authors(author_id))";
        execUpdate(authorBookTableQuery);
        connection.commit();
        connection.setAutoCommit(true);
    }

    public boolean delete(String title, String author, String publisher) {
        FullBook foundedBook = findBook(title, author, publisher);
        if (foundedBook == null)
            return false;

        fullBooks.remove(foundedBook);

        try (Connection con = GetJDBCConnection.init()) {
            String sql = "DELETE FROM full_books WHERE title = ?" +
                    "AND author = ? AND publisher = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, author);
                preparedStatement.setString(3, publisher);
                preparedStatement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addFullBook(FullBook fullBook) throws SQLException {
        try (Connection con = GetJDBCConnection.init()) {
            String sql = "INSERT INTO full_books VALUES(?, ?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, fullBook.getTitle());
            preparedStatement.setString(2, fullBook.getAuthor());
            preparedStatement.setInt(3, fullBook.getYearOfWriting());
            preparedStatement.setString(4, fullBook.getPublisherName());
            preparedStatement.executeUpdate();
        }
    }

    public void createFullBookTable() throws SQLException {
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String selectPublisherBookQuery = "create table if not exists full_books " +
                "as (select b.title as title, a.name as author, " +
                "b.yearOfWriting as year, p.name as publisher from books as b " +
                "inner join book_author as ba " +
                "on b.book_id=ba.book_id " +
                "inner join authors as a " +
                "on a.author_id=ba.author_id " +
                "inner join publishers as p " +
                "on p.publisher_id=b.publisher_id)";
        execUpdate(selectPublisherBookQuery);
        connection.commit();
        connection.setAutoCommit(true);
    }

    public List<String> list() {
        return fullBooks.stream().map(FullBook::getTitle)
                .collect(Collectors.toList());
    }

    public List<FullBook> getFullBooks() throws SQLException {
        fullBooks = new ArrayList<>();
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * from full_books");
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }

            return fullBooks;
        }
    }

    private FullBook findBook(String title, String author, String publisher) {
        if (fullBooks.isEmpty())
            return null;

        for (FullBook fullBook : fullBooks) {
            if (fullBook.getTitle().equals(title) && fullBook.getAuthor().equals(author) &&
                    fullBook.getPublisherName().equals(publisher)) {
                return fullBook;
            }
        }

        return null;
    }

    public boolean contains(String title, String author, String publisher) {
        if (fullBooks.isEmpty())
            return false;

        for (FullBook fullBook : fullBooks) {
            if (fullBook.getTitle().equals(title) & fullBook.getAuthor().equals(author) &
                    fullBook.getAuthor().equals(publisher)) {
                return true;
            }
        }

        return false;
    }

    //sort
    public List<FullBook> sortByTitle() throws SQLException{
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortTitleQuery = "SELECT t.* FROM test.full_books t ORDER BY title";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortTitleQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> sortByAuthor() throws SQLException{
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortAuthorQuery = "SELECT t.* FROM test.full_books t ORDER BY author";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortAuthorQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> sortByYear() throws SQLException{
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);

        String sortYearQuery = "SELECT t.* FROM test.full_books t ORDER BY year";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortYearQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> sortByPublisher() throws SQLException{
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortPublisherQuery = "SELECT t.* FROM test.full_books t ORDER BY publisher";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortPublisherQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> searchByTitle(String titleStr) throws SQLException {
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortPublisherQuery = "SELECT * FROM test.full_books WHERE title = '" + titleStr + "'";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortPublisherQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> searchByAuthor(String titleStr) throws SQLException {
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortPublisherQuery = "SELECT * FROM test.full_books WHERE author = '" + titleStr + "'";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortPublisherQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> searchByYear(String titleStr) throws SQLException {
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortPublisherQuery = "SELECT * FROM test.full_books WHERE year = '" + titleStr + "'";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortPublisherQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }

    public List<FullBook> searchByPublisher(String titleStr) throws SQLException {
        fullBooks = new ArrayList<>();
        connection.setAutoCommit(false);
        String sortPublisherQuery = "SELECT * FROM test.full_books WHERE publisher = '" + titleStr + "'";
        try (Connection con = GetJDBCConnection.init()) {
            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery(sortPublisherQuery);
            while (resultSet.next()) {
                String title = resultSet.getString(1);
                String author = resultSet.getString(2);
                int yearOfWriting = resultSet.getInt(3);
                String publisher = resultSet.getString(4);
                fullBooks.add(new FullBook(title, author, yearOfWriting, publisher));
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        return fullBooks;
    }



        //
//    private void generateAuthorsToTable() throws SQLException {
//        if (authors != null)
//            return;
//        connection.setAutoCommit(false);
//        authors = new ArrayList<>();
//        authors.add(new Author(1, "Fedor Dostoevsky"));
//        authors.add(new Author(2, "Aleksandr Solzhenitsyn"));
//        authors.add(new Author(3, "Smash Mouth"));
//        authors.add(new Author(4, "Lev Tolstoy"));
//        authors.add(new Author(5, "Ernest Hemingway"));
//        authors.add(new Author(6, "Aleksandr Pushkin"));
//        authors.add(new Author(7, "Someone"));
//
//        String authorsEntryQuery;
//        for (Author author : authors) {
//            authorsEntryQuery = "INSERT into authors " +
//                    "VALUES (" + author.getAuthorId() + ", '" + author.getName() + "')";
//            execUpdate(authorsEntryQuery);
//        }
//        connection.commit();
//        connection.setAutoCommit(true);
//    }
//
//    private void generatePublishersToTable() throws SQLException {
//        if (publishers != null)
//            return;
//        connection.setAutoCommit(false);
//        publishers = new ArrayList<>();
//        publishers.add(new Publisher(1, "AST"));
//        publishers.add(new Publisher(2, "EKSMO"));
//        publishers.add(new Publisher(3, "ROSMAN"));
//        publishers.add(new Publisher(4, "BOOK24"));
//
//        String publishersEntryQuery;
//
//        for (Publisher publisher : publishers) {
//            publishersEntryQuery = "INSERT into publishers " +
//                    "VALUES (" + publisher.getPublisherId() + ", '" +
//                    publisher.getPublisherName() + "')";
//            execUpdate(publishersEntryQuery);
//        }
//        connection.commit();
//        connection.setAutoCommit(true);
//    }
//
//    private void generateAuthorsBooksToTable() throws SQLException {
//        if (index != 0)
//            return;
//        index++;
//        connection.setAutoCommit(false);
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 1 AND a.author_id = 7");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 4 AND a.author_id = 1");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 8 AND a.author_id = 1");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 9 AND a.author_id = 1");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 10 AND a.author_id = 4");
//        execUpdate("INSERT INTO book_author(book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 2 AND a.author_id = 4");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 3 AND a.author_id = 3");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 7 AND a.author_id = 6");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 5 AND a.author_id = 5");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 6 AND a.author_id = 5");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 11 AND a.author_id = 2");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 12 AND a.author_id = 2");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 13 AND a.author_id = 2");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 14 AND a.author_id = 2");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 15 AND a.author_id = 1");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 16 AND a.author_id = 5");
//        execUpdate("INSERT INTO book_author (book_id, author_id) " +
//                "SELECT b.book_id, a.author_id," +
//                "FROM books b, authors a" +
//                "WHERE b.book_id = 17 AND a.author_id = 5");
//
//
//        connection.commit();
//        connection.setAutoCommit(false);
//    }

        //    private void generateBooksToTable() throws SQLException {
//        if (books != null)
//            return;
//        connection.setAutoCommit(false);
//        books = new ArrayList<>();
//        books.add(new Book(1, "Leo and Jungle", 2009, 2));
//        books.add(new Book(2, "War and Piece", 1858, 3));
//        books.add(new Book(3, "Somebody once told me", 1999, 1));
//        books.add(new Book(4, "Idiot", 1871, 2));
//        books.add(new Book(5, "Fiesta", 1929, 4));
//        books.add(new Book(6, "Farewell to arms", 1927, 4));
//        books.add(new Book(7, "Ruslan and Ludmilla", 1827, 1));
//        books.add(new Book(8, "The Brothers Karamazov", 1880, 1));
//        books.add(new Book(9, "Crime and Punishment", 1866, 1));
//        books.add(new Book(10, "Childhood", 1845, 1));
//        books.add(new Book(11, "The Gulag Archipelago", 1973, 1));
//        books.add(new Book(12, "The Gulag Archipelago", 1973, 2));
//        books.add(new Book(13, "The Gulag Archipelago", 1973, 3));
//        books.add(new Book(14, "The Gulag Archipelago", 1973, 4));
//        books.add(new Book(15, "The Brothers Karamazov", 1880, 2));
//        books.add(new Book(16, "Farewell to arms", 1927, 1));
//        books.add(new Book(17, "Fiesta", 1929, 2));
//
//        String bookEntryQuery;
//        for (Book book : books) {
//            bookEntryQuery = "INSERT into books " +
//                    "VALUES (" + book.getBookId() + ", '" + book.getTitle() + "', " +
//                    book.getYearOfWriting() + ", " + book.getPublisher_id() + ")";
//            execUpdate(bookEntryQuery);
//        }
//        connection.commit();
//        connection.setAutoCommit(true);
//    }

}

