<%@ page import="java.util.*" %>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.JsonObject"%>
<jsp:include page="../pages/template.jsp">
	<jsp:param value="Export Chart as Image" name="pageTitle"/>
	<jsp:param value="<div id='chartContainer'/>" name="content"/> 	
</jsp:include>

<%
Gson gsonObj = new Gson();
Map<Object,Object> map = null;
List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

map = new HashMap<Object,Object>(); map.put("y", 3);  map.put("legendText", "TDG");  map.put("label", "TDG"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 5);  map.put("legendText", "TDM");  map.put("label", "TDM	"); list.add(map);
map = new HashMap<Object,Object>(); map.put("y", 7);  map.put("legendText", "Total	");  map.put("label", "Real Estate"); list.add(map);
String dataPoints = gsonObj.toJson(list);
%>

<script type="text/javascript">

   $(function () {
       var chart = new CanvasJS.Chart("chartContainer", {
           title: {
               text: "Top Categories of New Year's Resolution"
           },
           exportFileName: "Pie Chart",
           exportEnabled: true,
           animationEnabled: true,
           legend: {
               verticalAlign: "bottom",
               horizontalAlign: "center"
           },
           data: [
           {
               type: "pie",
               showInLegend: true,
               toolTipContent: "{legendText}: <strong>{y}%</strong>",
               indexLabel: "{label} {y}%",
               dataPoints: <%out.print(dataPoints);%>
           }
           ]
       });
       chart.render();
   });
</script>