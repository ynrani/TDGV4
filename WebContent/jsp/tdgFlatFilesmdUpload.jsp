<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <title>TDG | Flat Files Operations</title>
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
		if("${uploadDTO.errors}" == null || typeof '${uploadDTO.errors}' == '' ||  '${uploadDTO.errors}' == ''){
			$('#errors').html('');
			$('#errors').hide();
			
		}else{
			 if('${uploadDTO.getErrors().size()}' > 0){
				var iLength = '${uploadDTO.getErrors().size()}';
				errorInfo = "";
		    	  for(i =0 ; i < iLength ; i++){
		    		  errorInfo += " \n "+"${uploadDTO.getErrors().get(i)}";
		    	  }
			$('#errors').html('${uploadDTO.errors}');
			$('#errors').show(); 
		}			 
		}
		
		if("${uploadDTO.messageConstant}" == 'SUCCESS'){
			 $('#success').html('${uploadDTO.message}');
				$('#success').show(); 
		 }else if("${uploadDTO.messageConstant}" == 'FAILED'){
			 $('#success').html('');
				$('#success').hide();
				$('#errors').html('${uploadDTO.message}');
				$('#errors').show();
		 }
		
	})
	function showAjaxLoad(){
	  $(".modal").show();
  }
  
  /* $(document).ready(function() {
  	//add more file components if Add is clicked
  	$('#addFile').click(function() {
  		var fileIndex = $('#fileTable tr').children().length - 1;
  		$('#fileTable').append(
  				'<tr><td>'+
  				'	<input type="file" name="files['+ fileIndex +']" class="form-control"/><a href="#" class="remove_field">Remove</a>'+
  				
  				'</td></tr>');
  	});
  	
  	$('#fileTable').on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parent('tr').remove();
    })
  	
  }); */
  
  $(document).ready(function() {
	    var max_fields      = 9; //maximum input boxes allowed
	    var wrapper         = $(".input_fields_wrap"); //Fields wrapper
	    //var add_button      = $(".add_field_button"); //Add button ID
	    
	    var x = 0; //initlal text box count
	    $('#addFile').click(function(e){ //on add input button click
	        e.preventDefault();
	         if(x < max_fields){ //max input box allowed
	            x++; //text box increment
	            $('#fileTable tr td').append('<div><input type="file" name="files['+x+']" /><a href="#" class="remove_field">Remove</a></div>'); //add input box
	        } 
	        /* var fileIndex = $('#fileTable tr').children().length - 1;
	  		$('#fileTable').append(
	  				'<tr><td>'+
	  				'	<input type="file" name="files['+ fileIndex +']" /><a href="#" onClick="deleteFile('+$(this).val()+');" class="remove_field">Remove</a>'+	  				
	  				'</td></tr>') */
	    });
	    
	    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
	        e.preventDefault(); 
	        //$(this).parent('tr').remove();
	    $(this).parent('div').remove(); x--;
	    })
	});
  function deleteFile(thisVal){
	  $(this).parent('tr').remove();
  }
  </script>

</head>
<body>
   <div id="main" class="wrapper">
    <!-- <script src="include/indexHeader.js"></script> -->
    <jsp:include page="indexHeader.jsp"></jsp:include>
      <div id="tabs-1" class="container">
		<form:form name="flatfilesupload" action="${pageContext.request.contextPath}/tdgUploadmdFiles" modelAttribute="uploadDTO" enctype="multipart/form-data">
		<div id="errors" class="errorblock" style = "display:none" ></div>
		<div id="success" class="successblock" style = "display:none" ></div>
		
		<!-- <div class="input_fields_wrap">
		<div><input type="file" name="files[0]" /></div> -->
		<div class="input_fields_wrap">
			<table id="fileTable" style="width:100%; border:0; font-size: 13px; padding-left: 90px; padding-right: 350px; padding-top: 35px;" >
			<tbody>	
				<tr>
				<td>
				
		<div><input type="file" name="files[0]" />
		<form:hidden path="manualDictionary" />
		</div>
				</td>
				</tr>			
				
			</tbody>	
			</table>
		
			</div>
			<table style="width:100%; border:0; font-size: 13px; padding-left: 90px; padding-right: 350px; padding-top: 35px;" >
			<tbody>	
			<%-- <tr>
				<td>What type file it is ? &nbsp; &nbsp; <form:select 
													path='manualDictionary'>
														<option value="MANUAL">Manual Dictionary</option>
														<option value="MASTER">Master Dictionary Table Creation</option>
														<option value="ALIAS">Table Alias</option>
												</form:select></td>
				<td></td> --%>
				<tr>
				<td><input type="button" value="Add File" class="btn-primary btn-cell"  id="addFile">&nbsp; &nbsp; <input type="submit" value="Submit" onclick="showAjaxLoad();" class="btn-primary btn-cell"  id="submit"></td>
				<td></td>
				</tr>			
				
			</tbody>	
			</table>
		</form:form>
	  </div>
  </div>
  <script>
 	menu_highlight('tdm_command_center1');
  </script>  
</body>
</html>