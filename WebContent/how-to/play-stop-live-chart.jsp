<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="../pages/template.jsp">
	<jsp:param value="Play / Stop Live Chart" name="pageTitle"/>
	<jsp:param value="<div id='chartContainer' style='height:400px; width:100%;'></div>
		<div style='margin-left:30%'>
		    <button id='StartButton' class='btn btn-success' type='submit' value='Start'>Start Live Chart</button>
		    <button id='StopButton' class='btn btn-danger' type='submit' value='Stop'>Stop Live Chart</button>
		</div><br />" name="content"/> 	
</jsp:include>

<script type="text/javascript">

   $(function () {
       var dps = [{ x: 1, y: 15 }, { x: 2, y: 20 }, { x: 3, y: 18 }, { x: 4, y: 22 }, { x: 5, y: 20 }];   //dataPoints.

       var chart = new CanvasJS.Chart("chartContainer", {
           title: {
               text: "Live Chart with Start/Stop Controls"
           },
           axisY: {
               title: "Units"
           },
           data: [{
               type: "spline",
               dataPoints: dps
           }]
       });

       chart.render();

       // Updating the Chart
       var xVal = dps.length + 1;
       var yVal = 20;
       var updateInterval = 500;

       var updateChart = function () {
           yVal = yVal + Math.round(5 + Math.random() * (-5 - 5));
           dps.push({ x: xVal, y: yVal });
           xVal++;
           chart.render();
       };

       var timeoutId,
         startButton = document.getElementById('StartButton'),
         stopButton = document.getElementById('StopButton');

       function startLiveChart() {
           timeoutId = setInterval(function () { updateChart() }, updateInterval);
           startButton.removeEventListener('click', startLiveChart);
           stopButton.addEventListener('click', stopLiveChart);
       }

       function stopLiveChart() {
           clearTimeout(timeoutId);
           stopButton.removeEventListener('click', stopLiveChart);
           startButton.addEventListener('click', startLiveChart);
       }

       startButton.addEventListener('click', startLiveChart);

   });
</script>