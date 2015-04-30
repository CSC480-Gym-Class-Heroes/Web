/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function getCurrentCount(gymName){
    //alert("HIOAJNFAJN");
    var millisecondsToWait = 500;
    setInterval(function() {
    // Whatever you want to do after the wait
    $.ajax({
        // url:"http://localhost:9999/Gym_Rats/getcurrentcount?gym=cooper",
        url:"getcurrentcount",
        data:{
            gym:gymName  
           },
            type:"GET",
            // dataType: 'text',
            //contentType: "application/json",
            success:function(response){
                //alert(response);               
                $("#currentCount").text(response);
            },
            error:function(xhr,sta,err){
               // alert("Get Current Count");
            }
        });
        }, millisecondsToWait);
}

function getInCount(gymName){
    //alert("HIOAJNFAJN");
    $.ajax({
        url:"getincount",
        data:{
          gym:gymName  
        },
        type:"GET",
        // dataType: 'text',
        //contentType: "application/json",
        success:function(response){
            $("#inCount").text(response);
        },
        error:function(xhr,sta,err){
            //alert("Get In Count");
        }
    });
}

function getAverageInCount(gymName){
    //alert("HIOAJNFAJN");
    $.ajax({
        //url:"http://pi.cs.oswego.edu:8080/Gym/getaverageincount?gym=glimmerglass",
        url:"getaverageincount",
        data:{
          gym:gymName  
        },
        type:"GET",
        // dataType: 'text',
        //contentType: "application/json",
        success:function(response){
            $("#averageInCount").text(response);
        },
        error:function(xhr,sta,err){
            //alert("Get Average Count error");
            $("#averageInCount").text(442);
        }
    });
}
/*
            <tr>
                <td>1</td>
                <td>Zumba</td>
                <td>1-2</td>
                <td>Mark</td>
            </tr>
 */

function generateTable(gymName){
     $.ajax({
         //http://localhost:9999/Gym_Rats/getgymclasses?gym=cooper
       url:"getgymclasses",
       data:{
         gym:gymName  
       },
        type:"GET",
       // dataType: 'text',
        //contentType: "application/json",
        success:function(response){
            //alert(response);
           // $("#currentCount").text(response);
           $.each(response,function(i, obj){
              // alert("Name:" + obj.name + " Desc:" + obj.description);
              var tr =document.createElement("tr");
              /*var td1 = document.createElement("td");
              td1.appendChild(document.createTextNode(i));
              */
              var td2 = document.createElement("td");
              td2.appendChild(document.createTextNode(obj.name));
              
              var td3 = document.createElement("td");
              td3.appendChild(document.createTextNode(obj.time));
              
              var td4 = document.createElement("td");
              td4.appendChild(document.createTextNode(obj.instructor));
              
              /*tr.appendChild(td1);*/
              tr.appendChild(td2);
              tr.appendChild(td3);
              tr.appendChild(td4);
              
              $("#classTable").append(tr);
              
           });
        },
        error:function(xhr,sta,err){
           // alert("Generate Table");
        }
    });
}

    function getHistoryData(gymName){
        var retVal;
        $.ajax({
       url:"getcountdata",
       data:{
         gym:gymName  
       },
        type:"GET",
       // dataType: 'text',
        //contentType: "application/json",
        success:function(response){
        retVal = response;
        },
        async:false,
        error:function(xhr,sta,err){
                //alert("History Date");
        }
    });
    return retVal;
}   	
        google.load('visualization', '1.0', {'packages':['corechart']});
        function drawChart(gymName) {
            var historyData = getHistoryData(gymName);
            var tableData = ([['People',  'Time']].concat(historyData));
            var data = google.visualization.arrayToDataTable(tableData);
            var options = {
                title: 'Attendence per Day',
                curveType: 'function',
                legend: { position: 'bottom' }
            };
				
            // Instantiate and draw our chart, passing in some options.
            function resize () {
                var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
                chart.draw(data, options);
            }
            window.onload = resize();
            window.onresize = resize;
            //var options = {'title':title,'width':w,'height':h,'chartArea':{left:0,top:10,width:"100%"}};
            //var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
            //chart.draw(data,options);
	}

		
