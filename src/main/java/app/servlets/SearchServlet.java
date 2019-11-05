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

public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/add.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String searchType = req.getParameter("search");


        if (searchType == null) {
            System.out.println("2");
            req.setAttribute("fail", "fail");
            new ListOfBooksServlet().doGet(req, resp);
            return;
        }

        try {
            req.setAttribute("searchType", searchType);
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/enterSearch.jsp");
            requestDispatcher.forward(req, resp);
        }
        catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("fail", "fail");
            new ListOfBooksServlet().doGet(req, resp);
        }


    }
}
