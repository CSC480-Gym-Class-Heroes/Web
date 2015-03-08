

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="Desktop.css">
        <title>Cooper History</title>
        <script type="text/javascript" src="https://www.google.com/jsapi?autoload={
                    'modules':[{'name': 'visualization', 'version': '1', 'packages': ['corechart']}]}"></script>

        <script type="text/javascript">
            google.setOnLoadCallback(drawChart);
            
            function drawChart(){
                
                var data = google.visulization.DataTable;
                data.addColumn('number', 'Time');
                data.addColumn('number', 'Number of People');
            
            
                var options = {
                    title: "Cooper history",
                    hAxis: {title: 'Time', minValue: 0, maxValue: 15},
                    vAxis: {title: 'People', minValue: 0, maxValue: 60}
                    legend: 'none'
                };

                var chart = new google.visualization.ScatterChart(document.getElementById('CooperHistory'));
                chart.draw(data, options);
            }
            
        </script>
        
    </head>
    <body>
        <h1>History on Cooper Gym</h1>
        <br>
        <div class="menu">
            <a href="index.jsp"> Current Data</a>
            <a href="GlimmerHistory.jsp"> Glimmer Glass History </a>
        </div>
        <br>
        <div id="CooperHistory" style="width: 900px; height: 500px;"> </div>
    </body>
</html>
