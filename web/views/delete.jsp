<%--
  Created by IntelliJ IDEA.
  User: Laplass
  Date: 29.10.2019
  Time: 22:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Delete book</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
</head>
<body>
    <div class="w3-container w3-blue-grey w3-opacity w3-center-align">
        <h1>Books</h1>
    </div>

    <div class="w3-container w3-padding">
        <%
            if (request.getAttribute("successDelete") != null)
                out.println("<div class=\"w3-panel w3-green w3-display-container w3-card-4 w3-round\">\n" +
                        "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                        "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-green w3-border w3-border-green w3-hover-border-grey\">×</span>\n" +
                        "   <h5>Book '" + request.getAttribute("successDelete") + "' was deleted!</h5>\n" +
                        "</div>");
            if (request.getAttribute("fail") != null)
                out.println("<div class=\"w3-panel w3-green w3-display-container w3-card-4 w3-round\">\n" +
                        "   <span onclick=\"this.parentElement.style.display='none'\"\n" +
                        "   class=\"w3-button w3-margin-right w3-display-right w3-round-large w3-hover-green w3-border w3-border-green w3-hover-border-grey\">×</span>\n" +
                        "   <h5>Failed adding<h5>\n" +
                        "</div>");
        %>
    </div>

    <div class="w3-card-4">
        <div class="w3-container w3-center w3-green">
            <h2>Delete book</h2>
        </div>

        <form method="post" class="w3-selection w3-light-grey w3-padding">
            <label>Title:
                <input type="text" name="title" class="w3-input w3-animate-input w3-border w3-round-large" style="width: 30%"><br />
            </label>
            <label>Author:
                <input type="text" name="author" class="w3-input w3-animate-input w3-border w3-round-large" style="width: 30%"><br />
            </label>
            <label>Publisher:
                <input type="text" name="publisher" class="w3-input w3-animate-input w3-border w3-round-large" style="width: 30%"><br />
            </label>
            <button type="submit">Submit</button>
        </form>

        <div class="w3-container w3-grey w3-opacity w3-right-align w3-padding">
            <button class="w3-btn w3-round-large" onclick="location.href='/LabSUBD/index.html'">Back to main menu</button>
        </div>
    </div>
</body>
</html>
