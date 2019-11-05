package app.servlets;

import app.entities.book.BookDatabase;
import app.entities.book.FullBook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnterSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/enterSearch.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchField = "";

        try
        {
            BookDatabase database = new BookDatabase();
            List<FullBook> fullBooks = new ArrayList<>();

            if (req.getParameter("title") != null) {
                searchField = req.getParameter("title");
                fullBooks = database.searchByTitle(searchField);
            } else if (req.getParameter("author") != null) {
                searchField = req.getParameter("author");
                fullBooks = database.searchByAuthor(searchField);
            } else if (req.getParameter("year") != null) {
                searchField = req.getParameter("year");
                fullBooks = database.searchByYear(searchField);
            } else if (req.getParameter("publisher") != null) {
                searchField = req.getParameter("publisher");
                fullBooks = database.searchByPublisher(searchField);
            }

            if (searchField == null || fullBooks == null) {
                System.out.println("3");
                req.setAttribute("fail", "fail");
                new ListOfBooksServlet().doGet(req, resp);
            }

            req.setAttribute("books", fullBooks);
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
