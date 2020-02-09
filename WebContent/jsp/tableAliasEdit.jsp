<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>TDG | Edit Table Alias</title>
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
 
</head>
<body>

	<div class="mainAll">

		<jsp:include page="indexHeader.jsp"></jsp:include>
		<section class="bodySec">
			<div class="container">
			  <form:form id="tdmUploadForm" name="tdmUploadForm" action="${pageContext.request.servletContext.contextPath}/editTdgTablealias" modelAttribute="tdgTableAliasDTO">
          	 	<div class="two-col">
          	 	<h2 style="color: #0098cc ;   padding-top: 5%;">Edit Table Alias</h2>
          	 	  <table style="width:100%; border:0; font-size: 13px; cellpadding:2;   border-spacing: 5px;  padding: 2% 0% 0% 0%; ">
          	 	  <form:hidden path="aliasid"/>
          	 	   <tbody>
          	 	   <%-- <tr>
                		<td class="lable-title" width="20%" align="left" valign="middle">Database Connections<span>*</span></td>
                		<td class="flied-title" width="30%" align="left" valign="middle">
                  		<form:select path="dbname" id="dbname" class="down-control">
	                      	<c:forEach items="${tdgTableAliasDTO.tdgConnections}" var="dbConnectionsDTOs" varStatus="status">
								<form:option value="${dbConnectionsDTOs}">${dbConnectionsDTOs}</form:option>
							</c:forEach>	
	                    </form:select>
	                    </td>
                 	  </tr> --%>
          	 	  	  <tr>
                		<td class="lable-title" width="20%" align="left" valign="middle">Table Name<span>*</span></td>
                		<td class="flied-title" width="30%" align="left" valign="middle">
                  		<form:input path="tabname" id="tabname" required="required"  class="form-control" autocomplete="off"/>
	                    </td>
                 	  </tr>
                 	  <tr>
                		<td class="lable-title" align="left" valign="middle">Alias<span>*</span></td>
                  		<td class="flied-title" align="left" valign="middle">
                    	   <form:input path="aliasname" id="aliasname" required="required"  class="form-control" autocomplete="off"/>
                  		</td>
                 	  </tr>
                	 <tr>
                		<td class="lable-title" align="left" valign="middle">Dictionary Name<span>*</span></td>
                  		<td class="flied-title" align="left" valign="middle">
                    	   <form:input path="schemaname" id="schemaname" required="required"  class="form-control" autocomplete="off"/>
                  		</td>
                 	  </tr>
                 	</tbody>          	 	
          	 	  </table>
          	 	  
          	 	  <c:if test="${status ne null}">
          	 	  	<table style="width:50%; border:0; font-size: 13px; color: #0B1CC5; padding: 1% 0% 0% 0%; ">
						<tbody>
						 <tr>
						   <td  align="center" valign="middle">
						   	${status}
						   </td>
						 </tr>
						</tbody>
				  </table>
          	 	  </c:if>
          	 	  <c:if test="${saveStatus ne null}">
          	 	  	<table style="width:50%; border:0; font-size: 13px; color: #0B1CC5; padding: 1% 0% 0% 0%; ">
						<tbody>
						 <tr>
						   <td  align="center" valign="middle">
						   	 ${saveStatus}
						   </td>
						 </tr>
						</tbody>
				  </table>
          	 	  </c:if>
				  <table style="width:50%; border:0; font-size: 13px; cellpadding:4; padding: 1% 0% 0% 0%; ">
					<tbody>
					 <tr>
					   <td colspan="2" align="center" valign="middle" class="buttonsAll22">
					  	 <input type="submit" name="testCon" id="testCon" value="Validate Alias">
					  	 <input type="submit" name="create" id="create" value="Save">
					   </td>
					 </tr>
					</tbody>
				  </table>
				  
         	    </div>
         	  </form:form>
			</div>
		</section>
		<script src="include/footer.js"></script>
	</div>
	<script>
		menu_highlight('admin');
		menu_highlight('admin_db_connection');
		menu_highlight('db_connection');
		
		
		$(document).ready(function() {
			var btn =${btn};
		     if(btn  == false){
		        $('#create').prop('disabled', false);
		     }else{
		    	$('#create').prop('disabled', true);
		     }
		 });
	  
	</script>
</body>
</html>
