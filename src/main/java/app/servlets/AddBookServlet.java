package app.servlets;

import app.entities.book.BookDatabase;
import app.entities.book.FullBook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import static org.graalvm.compiler.options.OptionType.User;

public class AddBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/add.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String yearOfWritingString = req.getParameter("year");
        String publisher = req.getParameter("publisher");

        try {
            FullBook fullBook = new FullBook(title, author, Integer.parseInt(yearOfWritingString), publisher);
            BookDatabase bookDatabase = new BookDatabase();
            bookDatabase.addFullBook(fullBook);
            req.setAttribute("bookTitle", fullBook.getTitle());
            doGet(req, resp);
        }
        catch (Exception e) {
            req.setAttribute("fail", "fail");
            doGet(req, resp);
        }


    }
}
