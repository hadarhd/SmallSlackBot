<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="com.doodle.smallSlackBot.User" %>
<%--
  Created by IntelliJ IDEA.
  User: hhasday
  Date: 3/5/2019
  Time: 17:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%

        String userName = (String)request.getAttribute("UserName");
        if (User.usersNamesToIDs.containsKey(userName)) {
            %>
             <h1>The current average for the Slack user name <%=userName%> is: <%=User.getAvgPerUser(userName)%>.</h1>
            <%
        }
        else {
            %>
        <h1>The user name <%=userName%> is not a legal Slack user name.</h1>
    <%
        }
    %>


</body>
</html>
