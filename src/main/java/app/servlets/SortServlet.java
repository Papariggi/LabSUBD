package app.servlets;

import app.entities.book.BookDatabase;
import app.entities.book.FullBook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SortServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("fail", "fail");
        new ListOfBooksServlet().doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sortType = req.getParameter("sort");

        if (sortType == null) {
            System.out.println(1);
            req.setAttribute("fail", "fail");
            new ListOfBooksServlet().doGet(req, resp);
        }

        try {
            BookDatabase database = new BookDatabase();
            List<FullBook> fullBooks;

            switch (sortType) {
                case("sortByTitle") : {
                    fullBooks = database.sortByTitle();
                    req.setAttribute("books", fullBooks);
                    break;
                }
                case("sortByAuthor") : {
                    fullBooks = database.sortByAuthor();
                    req.setAttribute("books", fullBooks);
                    break;
                }
                case("sortByYear") : {
                    fullBooks = database.sortByYear();
                    req.setAttribute("books", fullBooks);
                    break;
                }
                case("sortByPublisher") : {
                    fullBooks = database.sortByPublisher();
                    req.setAttribute("books", fullBooks);
                    break;
                }
                default:
                    req.setAttribute("fail", "fail");
                    new ListOfBooksServlet().doGet(req, resp);
                    break;
            }

            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/list.jsp");
            requestDispatcher.forward(req, resp);
        }
        catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("fail", "fail");
            new ListOfBooksServlet().doGet(req, resp);
        }
    }
}
