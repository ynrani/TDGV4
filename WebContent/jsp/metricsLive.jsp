<%@page import="com.tesda.model.DTO.MetricsDetailsDTO"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.*" %>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.JsonObject"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <title> TDG | Metrics Live</title>
<link rel="stylesheet" type="text/css" href="css/jquery-ui.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/custom.css">
<link rel="stylesheet" type="text/css" href="css/demo.css" />
<link rel="stylesheet" type="text/css" href="css/style1.css" />
<link rel="stylesheet" type="text/css" href="css/animate-custom.css" />
<link rel="stylesheet" type="text/css" href="css/menu.css" />
<link rel="stylesheet" type="text/css" href="css/theme.default.css">
<link rel="stylesheet" type="text/css" href="css/stylesNew.css">
    
<script type="text/javascript" src="js/html5.js"></script>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/main.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="js/script.js"></script>

<script
	src="assets/script/bootstrap.min.js"
	type="text/javascript"></script>
<script src="assets/script/canvasjs.min.js"
	type="text/javascript"></script>
<%
Gson gsonObj = new Gson();
Map<Object,Object> map = null;
MetricsDetailsDTO metricsDetailsDTO=  (MetricsDetailsDTO)request.getAttribute("metricsDetailsDTO");
String dataPoints1 = gsonObj.toJson(metricsDetailsDTO.getDataPoint1());

String dataPoints2 = gsonObj.toJson(metricsDetailsDTO.getDataPoint2());
	
String dataPoints3 = gsonObj.toJson(metricsDetailsDTO.getDataPoint3());

String dataPoints4 = gsonObj.toJson(metricsDetailsDTO.getDataPoint4());
%>
          
<script type="text/javascript">

$(function () {
    //var dps = [{ x: 1, y: 10 }, { x: 2, y: 13 }, { x: 3, y: 18 }, { x: 4, y: 20 }, { x: 5, y: 17 }, { x: 6, y: 10 }, { x: 7, y: 13 }, { x: 8, y: 18 }, { x: 9, y: 20 }, { x: 10, y: 17 }];   //dataPoints.
var dps = [];
    var chart = new CanvasJS.Chart("chartContainer", {
        title: {
            text: "Dynamic / Live Chart"
        },
        data: [{
            type: "line",
            dataPoints: dps
        }]
    });

    chart.render();

    var xVal = dps.length + 1;
    var yVal = 15;
    var updateInterval = 500;

    var updateChart = function () {
        yVal = yVal + Math.round(5 + Math.random() * (-5 - 5));
        //alert(yVal);
        dps.push({ x: xVal, y: yVal });
        xVal++;

        chart.render();
    };
    setInterval(function () { updateChart() }, updateInterval);
});
</script>
</head>

<body>
  <div id="main" class="mainAll">
   <!--  <script src="include/header.js"></script> -->
   <jsp:include page="indexHeader.jsp"></jsp:include>
      <div id="tabs-1" class="container">
        <form:form id="metricsBoardForm" name="metricsBoardForm" action="${pageContext.request.contextPath}/metricslive" modelAttribute="metricsDetailsDTO">
      <div class="row">
				<div id="content" class="col-md-8 col-md-offset-1 col-xs-12">
					<div id='chartContainer' style='border-style: ridge;'></div>
		
				</div>
			</div>

    
        </form:form>
      </div>
    <script src="include/footer.js"></script>
  </div>
   
 
</body>
</html>
 