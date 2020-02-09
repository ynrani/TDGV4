<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
  <title>TDG | Create Master Dictionary</title>
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
<style type="text/css">
table{
        width: 100%;
        margin-bottom: 20px;
		border-collapse: collapse;
    }
    table, th, td{
        border: 1px solid #cdcdcd;
    }
    table th, table td{
        padding: 10px;
        text-align: left;
    }
</style>
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
  
  $(document).ready(function(){
	  if('${tdgMasterDictionaryDTO.editFlag}' === true || '${tdgMasterDictionaryDTO.editFlag}' ==='true'){
	  var str = "${tdgMasterDictionaryDTO.conSelected}";
	  //alert("${tdgMasterDictionaryDTO.conSelected}");
	  //str = str.replace("TDGCON");
	  if(str.toLowerCase().indexOf("TDGCON") >= 0){
	  var strArray = str.split('TDGCON');

      for (var i = 0; i < strArray.length; i++) {
        //alert(countryArray[i]);
    	  var markup = "<tr><td align='left'><input type='checkbox' name='record' title='"+strArray[i]+"'></td><td>" + strArray[i] + "</td></tr>";
          $("table tbody").append(markup);
          $("#connection option[value='"+name+"']").remove();
      }
	  }else{
		  console.log(str);
		  var markup = "<tr><td align='left'><input type='checkbox' name='record' title='"+str+"'></td><td>" + str + "</td></tr>";
          $("table tbody").append(markup);
          $("#connection option[value='"+str+"']").remove();
	  }
	  }

      $("#add-row").click(function(){
          var name = $("#connection").val();
          var markup = "<tr><td align='left'><input type='checkbox' name='record' title='"+name+"'></td><td>" + name + "</td></tr>";
          $("table tbody").append(markup);
          $("#connection option[value='"+name+"']").remove();
      });
      
      // Find and remove selected table rows
      $("#deleterow").click(function(){
    	  var x = 0;
          $("table tbody").find('input[name="record"]').each(function(){
          	if($(this).is(":checked")){
          		$("#connection").append("<option value='"+$(this).attr("title")+"'>"+$(this).attr("title")+"</option>");
                  $(this).parents("tr").remove();
                  
                  //alert($(this).val());
                  
              }
          	x++;
          });
          if(x == 0){
        	  alert("Select atleast one connection from table view");
          }
      });
      
      $("#submit").click(function(){
    	  var selectedVal = '';
      $('#fileTable td').each(function() {
          var text = $(this).text();
          if (text != null && text != '') {
        	// Your Code Here
        	  if( selectedVal != '')
        		  selectedVal +='TDGCON'+text;
        	  else selectedVal +=text;
        	}
          //alert(text);
      });
      if($("#dictionaryName").val() == null || $("#dictionaryName").val() == ''){
    	  alert("Enter Dictionary Name");
    	  return;
      }
      if(selectedVal == null || selectedVal == ''){
    	  alert("Add atleast one connection name");
    	  return;
      }
      document.location.href="./tdgaCreateMasterDictionary?conSelected="+selectedVal+"&dictionaryName="+$("#dictionaryName").val();
      });
      
  });    
  
  </script>

</head>
<body>
   <div id="main" class="wrapper">
    <!-- <script src="include/indexHeader.js"></script> -->
    <jsp:include page="indexHeader.jsp"></jsp:include>
      <div id="tabs-1" class="container">
		<form:form name="flatfilesupload" action="${pageContext.request.contextPath}/tdgaCreateMasterDictionary" modelAttribute="tdgMasterDictionaryDTO" >
		<div id="errors" class="errorblock" style = "display:none" ></div>
		<div id="success" class="successblock" style = "display:none" ></div>
		
		<!-- <div class="input_fields_wrap">
		<div><input type="file" name="files[0]" /></div> -->
		Dictionary Name &nbsp; &nbsp; <form:input type="text" id="dictionaryName" path="dictionaryName" required="required" placeholder="Enter Master Dictionary Name" class="form-control-half-dictionary"/>
		<div style="display: block;width=90%;">
		Connection Names &nbsp;<select id="connection" class="form-control-half-dictionary">
		<c:forEach items="${tdgMasterDictionaryDTO.tdgConnections}" var="dbConnectionsDTOs" varStatus="status">
		<option value="${dbConnectionsDTOs}">${dbConnectionsDTOs}</option>
		</c:forEach>	
		</select> &nbsp;
		<input type="button" class="btn-primary btn-cell" id="add-row" value="Add Connections">
		</div>
		<br/>
		<!-- 
        <input type="text" id="email" placeholder="Email Address" class="form-control">
    	<input type="button" class="btn-primary btn-cell" id="add-row" value="Add Row"> -->
			<table id="fileTable" style="width:40%;font-size: 13px;border:0; cellpadding:0; cellspacing:1">
        <thead>
            <tr>
                <th  bgcolor="#E3EFFB" scope="col" class="whitefont" align="center">Select</th>
                <th  bgcolor="#E3EFFB" scope="col" class="whitefont" align="center">Connection Name</th>
            </tr>
        </thead>
        <tbody>
          <!--   <tr>
                <td align="left"><input type="checkbox" name="record"></td>
                <td align="left">Peter Parker</td>
                <td align="left">peterparker@mail.com</td>
            </tr>
 -->        </tbody>
    </table>
    <input type="button" id="deleterow" class="btn-primary btn-cell" value="Delete Row"/>
    <input type="button" id="submit" class="btn-primary btn-cell" value="Submit" />
    
		</form:form>
	  </div>
  </div>
  <script>
 	menu_highlight('tdm_command_center1');
  </script>  
</body>
</html>