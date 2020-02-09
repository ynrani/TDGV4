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
  <title> TDG | Metrics</title>
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


List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

map = new HashMap<Object,Object>(); map.put("y", 111338);  map.put("label", "DB"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 49088);  map.put("label", "XLS"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 62200);  map.put("label", "CSV"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 90085);  map.put("label", "XML"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 38600);  map.put("label", "XLSFL"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 48750);  map.put("label", "CSVLS"); list.add(map);	
String dataPoints11 = gsonObj.toJson(list);

list = new ArrayList<Map<Object,Object>>();
map = new HashMap<Object,Object>(); map.put("y", 1355);  map.put("label", "DB"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 1072);  map.put("label", "XLS"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 523);  map.put("label", "CSV"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 336);  map.put("label", "XLSFL"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 3990);  map.put("label", "CSVLS"); list.add(map);
String dataPoints12 = gsonObj.toJson(list);
%>
          
<script type="text/javascript">

   $(function () {
	   var dps = [];
       var chart1 = new CanvasJS.Chart("chartContainer1",{
           title:{
               text: "Generated Records Count"
           },
           axisY :{
               title: "Total Records",
               titleFontSize: 18//,
               //suffix: " kPa"
           },
           animationEnabled: true,
          
           data: [
           {
               markerSize: 0,
               toolTipContent: "<span style='\"'color: {color};'\"'><strong>Records Generated</strong></span> {y}",
       type: "spline",
       showInLegend: true,
       legendText: "Records generated",
       dataPoints: <%out.print(dataPoints1);%>
   }

   ]
   });

   chart1.render();

   var chart2 = new CanvasJS.Chart("chartContainer2", {
       title: {
           text: "TDG Generation Type details"
       },
       animationEnabled: true,
       legend: {
           verticalAlign: "center",
           horizontalAlign: "left",
           fontSize: 18,
           fontFamily: "Helvetica"
       },
       theme: "theme2",
       data: [
       {
           type: "pie",
           indexLabelFontFamily: "Garamond",
           indexLabelFontSize: 20,
           indexLabel: "{label} {y}%",
           startAngle: -20,
           showInLegend: true,
           toolTipContent: "{legendText} {y}%",
           dataPoints: <%out.print(dataPoints2);%>
       }
       ]
   });
   chart2.render();

   var chart3 = new CanvasJS.Chart("chartContainer3",{
       theme: "theme2",
       animationEnabled: true,
       title: {
           text: "Generation Type Count"
       },
       axisY: {
           title: "Total"
       },
       data: [{
           type: "column",
           showInLegend: true,
           legendMarkerColor: "grey",
           //legendText: "MMbbl = one million barrels",
           dataPoints: <%out.print(dataPoints3);%>
       }]
   });
   chart3.render();

   var chart4 = new CanvasJS.Chart("chartContainer4",{
	   animationEnabled: true,
       title: {
           text: "Success Rate Calculte"
       },
       axisY: {
           title: "Total Count"//,
           /* valueFormatString: "#0.#,.", */
       },
       data: [
       {
           type: "stackedColumn",
           legendText: "Success",
          /*  yValueFormatString: "#0.#,.", */
           showInLegend: "true",
           dataPoints: <%out.print(dataPoints11);%>
       }, 
       {
           type: "stackedColumn",
           legendText: "Failure",
           showInLegend: "true",
           indexLabel: "#total ",
           /* yValueFormatString: "#0.#,.",
           indexLabelFormatString: "#0.#,.", */
           indexLabelPlacement: "outside",
           dataPoints: <%out.print(dataPoints12);%>
       }
       ]
   });
   chart4.render();

   });
</script>
</head>

<body>
  <div id="main" class="mainAll">
   <!--  <script src="include/header.js"></script> -->
   <jsp:include page="indexHeader.jsp"></jsp:include>
      <div id="tabs-1" class="container">
        <form:form id="metricsBoardForm" name="metricsBoardForm" action="${pageContext.request.contextPath}/metricsDetails" modelAttribute="metricsDetailsDTO">
      <div class="row">
				<div id="content" class="col-md-8 col-md-offset-1 col-xs-12">
					<div id='chartContainer1' style='width: 49%; height: 300px;display: inline-block;border-style: ridge;'></div>
		<div id='chartContainer2' style='width: 49%; height: 300px;display: inline-block;border-style: ridge;'></div>
		<br /><br />
		<div id='chartContainer3' style='width: 49%; height: 300px;display: inline-block;border-style: ridge;'></div>
		<div id='chartContainer4' style='width: 49%; height: 300px;display: inline-block;border-style: ridge;'></div>
				</div>
			</div>

    
        </form:form>
      </div>
    <script src="include/footer.js"></script>
  </div>
   
 
</body>
</html>
 