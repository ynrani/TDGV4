<%@ page import="java.util.*" %>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.JsonObject"%>

<jsp:include page="../pages/template.jsp">
	<jsp:param value="Line Chart" name="pageTitle"/>
	<jsp:param value="<div id='chartContainer'/>" name="content"/> 	
</jsp:include>
<%
Gson gsonObj = new Gson();
Map<Object,Object> map = null;
List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

map = new HashMap<Object,Object>(); map.put("x", 1);  map.put("y", 71);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 2);  map.put("y", 55);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 3);  map.put("y", 50);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 4);  map.put("y", 65);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 5);  map.put("y", 95);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 6);  map.put("y", 68);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 7);  map.put("y", 28);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 8);  map.put("y", 34);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 9);  map.put("y", 14);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 10);  map.put("y", 33);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 11);  map.put("y", 42);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 12);  map.put("y", 62);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 13);  map.put("y", 70);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 14);  map.put("y", 85);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 15);  map.put("y", 58);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 16);  map.put("y", 34);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 17);  map.put("y", 24);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 18);  map.put("y", 33);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 19);  map.put("y", 28);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 20);  map.put("y", 42);list.add(map);
map = new HashMap<Object,Object>(); map.put("x", 30);  map.put("y", 42);list.add(map);	
String dataPoints = gsonObj.toJson(list);
%>

<script type="text/javascript">

   $(function () {
       var chart = new CanvasJS.Chart("chartContainer", {
           theme: "theme2",
           zoomEnabled: true,
           animationEnabled: true,
           title: {
               text: "Line Chart in JSP using CanvasJS"
           },
           subtitles: [
               {
                   text: "Try Zooming and Panning"
               }
           ],
           data: [{
               type: "line",
               dataPoints: <%out.print(dataPoints);%>
           }]
       });
       chart.render();
   });
</script>