<%--
  Created by IntelliJ IDEA.
  User: Laplass
  Date: 05.11.2019
  Time: 3:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add new Book</title>
        <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    </head>

<body>
    <div class="w3-container w3-blue-grey w3-opacity w3-center-align">
        <h1>Books</h1>
    </div>

    <div class="w3-card-4">
        <div class="w3-container w3-center w3-green">
            <h2>Search book</h2>
        </div>

        <form method="post" action = "/LabSUBD/enterSearch" class="w3-selection w3-light-grey w3-padding">
            <%
                switch (request.getAttribute("searchType").toString()) {
                    case ("searchByTitle") : {
                        out.println("<label>Title:\n" +
                                "                <input type=\"text\" name=\"title\" class=\"w3-input w3-animate-input w3-border w3-round-large\" style=\"width: 30%\"><br />\n" +
                                "            </label>");
                        break;
                    }
                    case ("searchByAuthor") : {
                        out.println("<label>Author:\n" +
                                "                <input type=\"text\" name=\"author\" class=\"w3-input w3-animate-input w3-border w3-round-large\" style=\"width: 30%\"><br />\n" +
                                "            </label>");
                        break;
                    }
                    case("searchByYear") : {
                        out.println("<label>Year of writing:\n" +
                                "                <input type=\"text\" name=\"year\" class=\"w3-input w3-animate-input w3-border w3-round-large\" style=\"width: 30%\"><br />\n" +
                                "            </label>");
                        break;
                    }
                    case("searchByPublisher") : {
                        out.println("<label>Publisher:\n" +
                                "                <input type=\"text\" name=\"publisher\" class=\"w3-input w3-animate-input w3-border w3-round-large\" style=\"width: 30%\"><br />\n" +
                                "            </label>");
                        break;
                    }
                }
            %>
            <button type="submit">Submit</button>
        </form>

        <div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
            <button class="w3-btn w3-round-large" onclick="location.href='/LabSUBD/index.html'">Back to main menu</button>
        </div>
    </div>
</body>
</html>
