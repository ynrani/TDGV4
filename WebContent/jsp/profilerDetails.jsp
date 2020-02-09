<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!doctype html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>TDG | Profile Details</title>
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
 $(function() {
	 //$(".modal").hide();
 });
 </script>
 
</head>
<body>

	<div class="mainAll">

		<section class="bodySec">
			<div class="container">
			  <form:form id="tdgProfilerform" name="tdgProfilerform" action="${pageContext.request.servletContext.contextPath}/tdgProfilerDetails" modelAttribute="tdgProfileDTO">
          	 	<div class="two-col">
          	 	<h2 style="color: #0098cc ;   padding-top: 5%;">Profile Details</h2>
          	 	  <table style="width:100%; border:0; font-size: 13px; cellpadding:2;   border-spacing: 5px;  padding: 2% 0% 0% 0%; ">
          	 	  
          	 	   <tbody>
          	 	   <tr>
                		<td class="lable-title" width="20%" align="left" valign="middle">Dictionary Name</td>
                		<td class="flied-title" width="20%" align="left"
												valign="middle"><label id="dictionaryname"></label></td>                		
                 	  </tr>
          	 	  	  <tr>
                		<td class="lable-title" width="20%" align="left" valign="middle">Data Base Type</td>
                		<td class="flied-title" width="20%" align="left"
												valign="middle"><label id="databasetype"></label></td>                		
                 	  </tr>
                 	  <tr>
                		<td class="lable-title" width="20%" align="left" valign="middle">Profile Name<span>*</span></td>
                		<td class="flied-title" width="20%" align="left"
												valign="middle">
							<form:input path="profileName" id="profileName" required="required"  class="form-control" autocomplete="off"/>
						</td>                		
                 	  </tr>
                 	
                 	  
                 	  
					</tbody>          	 	
          	 	  </table>
				  <table style="width:50%; border:0; font-size: 13px; cellpadding:4; padding: 1% 0% 0% 0%; ">
					<tbody>
					 <tr>
					   <td colspan="2" align="center" valign="middle" class="buttonsAll22">
					  	 <input type="submit" name="save" id="save" value="Save Profile">
					  	 <input type="submit" name="cancel" id="cancel" value="Cancel">
					  	 
					   </td>
					 </tr>
					</tbody>
				  </table>
				  
         	    </div>
         	  </form:form>
			</div>
		</section>
		
	</div>
	<script>
		//menu_highlight('admin');
	</script>
</body>
</html>
