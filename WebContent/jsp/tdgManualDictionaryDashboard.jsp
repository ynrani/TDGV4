<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <title>TDG | Manual Dictionary</title>
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
  <script>
  $(document).ready(function() {
	  $('#errors').html('');
		$('#errors').hide();
		$('#success').html('');
		$('#success').hide();
		if("${error}" == null || typeof '${error}' == '' ||  '${error}' == ''){
			$('#error').html('');
			$('#error').hide();
			
		}else{
			$('#error').html('${error}');
			$('#error').show(); 
		}			
	})
  </script>

</head>
<body>
   <div id="main" class="wrapper">
    <!-- <script src="include/indexHeader.js"></script> -->
    <jsp:include page="indexHeader.jsp"></jsp:include>
      <div id="tabs-1" class="container">
		
 <form:form name="manualDictionary"
				action="${pageContext.request.contextPath}/tdgaManualDictionaryDashboard" modelAttribute="baseDTO">
				<div id="errors" class="errorblock" style="display: none"></div>
				<div id="success" class="successblock" style="display: none"></div>
				<c:choose>
					<c:when
						test="${listDictionaries ne null && not empty listDictionaries }">
						<div class="scrollingX" id="myid">
							<table id="search_output_table" class="hoverTable" 
								style="width: 100%; font-size: 13px;">
								<thead>
									<tr>
										<th align="center" bgcolor="#E3EFFB" scope="col"
											class="whitefont">Manual Dictionary Name</th>
										<th align="center" bgcolor="#E3EFFB" scope="col"
											class="whitefont">Action</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${listDictionaries}" var="listDictionaries"
										varStatus="status">
										<tr>
											<td align="left">${listDictionaries}</td>
											<c:choose>
													<c:when
														test="${fn:contains(listDictionaries, 'EXIST')}">
														<td align="left"></td>
													</c:when>
													<c:otherwise>
														<td align="center"><a
															href="./tdgaManualDictionaryDashboard?dictionaryname=${listDictionaries}">Delete</a></td>
													</c:otherwise>
													</c:choose>
											
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

						

					</c:when>
					<c:otherwise>
						<h3
							style="float: left; width: 40%; border: 0; font-size: 14px; color: black; padding-top: 15px">
							<u>No TDG Manual Dictionaries are available</u>
						</h3>
						<br />
					</c:otherwise>
				</c:choose>
			</form:form>
	</div>
	<script src="include/footer.js"></script>
  </div>		
</body>
  <script>
  menu_highlight('tdm_command_center1');
  $("#search_output_table").tablesorter({
	    widgets: ['zebra']
	  });
  
  function deleteDictionary(reqId){
 		 if (confirm('Are you sure to delete the dictionary?')) {
 	 	   document.location.href="./tdgaManualDictionaryDashboard?dictionaryname="+reqId;
 		  }
  }
  
  
  </script>  
</body>
</html>