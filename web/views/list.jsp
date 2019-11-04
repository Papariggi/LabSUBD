<%@ page import="java.util.List" %>
<%@ page import="app.entities.book.FullBook" %><%--
  Created by IntelliJ IDEA.
  User: Laplass
  Date: 24.10.2019
  Time: 17:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of books</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
    <div class="w3-container w3-blue-grey w3-opacity w3-center-align">
        <h1>Books</h1>
    </div>

    <div class="w3-container w3-center w3-margin-bottom w3-padding">
            <div class="w3-container w3-light-blue">
                <h2>Books</h2>
            </div>
            <div>
                <table class="w3-table w3-striped">
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Year of Writing</th>
                        <th>Publisher</th>
                    </tr>
                    <%
                   List<FullBook> books = (List<FullBook>) request.getAttribute("books");
                   if (books != null && !books.isEmpty()) {
                       for (FullBook book : books) {
                           out.println("<tr>");

                           out.print("<td>" + book.getTitle() + "</td>\n" +
                                   "<td>" + book.getAuthor() + "</td>\n" +
                                   "<td>" + book.getYearOfWriting() + "</td>\n" +
                                   "<td>" + book.getPublisherName() + "</td>\n");

                           out.println("</tr>");
                       }
                   }
                   else
                       out.println("There no books");
                    %>
                </table>
            </div>

    <div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
        <button class="w3-btn w3-round-large" onclick="location.href='/LabSUBD/index.html'">Back to main menu</button>
    </div>

</body>
</html>
