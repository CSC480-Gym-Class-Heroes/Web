
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Homepage</title>
        <link rel="stylesheet" type="text/css" href="Desktop.css">
    </head>
    <body>
        
        <%  int cDailyAvg = 20;
            int cCurrentNum = 10;
            int cDailyTotal = 100;
        %>
        
        <h1> <a href="index.jsp">Gym Class Heroes </a> </h1>
        <div class="menu">
            <a href="CooperHistory.jsp"> Cooper History </a>
            <a href="GlimmerHistory.jsp"> Glimmer Glass History </a>
        </div>
        
        <div> <h2> Cooper Gym </h2> </div>
        
        <div class="cooperContainer"> 
            Current <b><%= cCurrentNum %> </b>
        </div>
        <div class="cooperContainer"> 
            Daily total <b><%= cDailyTotal %> </b>
        </div>
        <div class="cooperContainer"> 
            Daily average <b><%=cDailyAvg %></b>
        </div>
        <br>
        <div> <h2 class="glimmerHeader"> Glimmer Glass Gym </h2> </div>
        <div class="glimmerContainer"> Current </div>
        <div class="glimmerContainer"> Daily total </div>
        <div class="glimmerContainer"> Daily average </div>
        
    </body>
</html>
