package app.servlets;

import app.entities.book.BookDatabase;
import app.entities.book.FullBook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class DeleteBookServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/delete.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String author = req.getParameter("author");
        String publisher = req.getParameter("publisher");


        try {
            BookDatabase bookDatabase = new BookDatabase();
            List<FullBook> fullBooks = bookDatabase.getFullBooks();

            if (fullBooks.isEmpty() || !bookDatabase.delete(title, author, publisher)) {
                req.setAttribute("notFound", "notFound");
                doGet(req, resp);
                return;
            }

            req.setAttribute("successDelete", title);
            doGet(req, resp);
        }
        catch (Exception e) {
            req.setAttribute("fail", "fail");
            doGet(req, resp);
        }
    }
}
