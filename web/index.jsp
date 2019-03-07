<%--
  Created by IntelliJ IDEA.
  User: hhasday
  Date: 3/2/2019
  Time: 21:12
  To change this template use File | Settings | File Templates.
--%>
<%@page import="com.doodle.smallSlackBot.Bot"%>
<%@page import="com.doodle.smallSlackBot.User"%>


<%! public void jspInit() {
    Bot.main(null);
}
%>
<html>
  <head></head>
  <body>
    <h1>Current total average is: <%=User.getTotalAvg()%></h1>
  </body>
</html>
