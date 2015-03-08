<%-- 
    Document   : GlimmerHistory.jsp
    Created on : Mar 8, 2015, 12:09:33 PM
    Author     : Emma
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Glimmer History</title>
        <link rel="stylesheet" type="text/css" href="Desktop.css">
        <script type="text/javascript" src="https://www.google.com/jsapi?autoload={
                    'modules':[{'name': 'visualization', 'version': '1', 'packages': ['corechart']}]}"></script>

        <script type="text/javascript">
        
        google.setOnLoadCallback(drawChart);          
        function drawChart(){

            var data = new google.visualization.DataTable();
            data.addColumn('number', 'Time');
            data.addColumn('number', 'Number of People');
            data.addRows([
                [1, 5],
            ]);


            var options = {
                title: "Cooper history",
                hAxis: {title: 'Time', minValue: 0, maxValue: 15},
                vAxis: {title: 'People', minValue: 0, maxValue: 60},
                legend: 'none'
            };

            var chart = new google.visualization.ScatterChart(document.getElementById('glimmerHistory'));
            chart.draw(data, options);
        }
        </script>
    </head>
    <body>
               <h1>Data for Glimmer Glass Gym</h1>
        <br>
        <div class="menu">
            <a href="index.jsp"> Current Data</a>
            <a href="CooperHistory.jsp"> Cooper Glass History </a>
        </div>
        <br>
        <div id="glimmerHistory" style="width: 900px; height: 500px;"> </div>
    </body>
</html>
