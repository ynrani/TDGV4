<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
<title>Add Tables</title>
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
 
	function showAjaxLoad(){
	  $(".modal").show();
  }
	$(document).ready(function() {
		  var strFlag = "${tdgMasterDictionaryDTO.flagSelected}";
		  var str = "${tdgMasterDictionaryDTO.selectedTabs}";
		  if(str != '' && str != undefined && strFlag != '' && strFlag != undefined){
		  if (str.indexOf(",") >= 0) {

				var dependsArray = str.split(',');
				for (var ii = 0; ii < dependsArray.length; ii++) {
					$("#selectedTabsTemp").append("<option value='"+dependsArray[ii]+"'>"+dependsArray[ii]+"</option> \n");
				}
			}else
				{
				$("#selectedTabsTemp").append("<option value='"+dependsArray[ii]+"'>"+dependsArray[ii]+"</option> \n");
				}
		  }
	});
  $(function(){
	    $("#rightArrow").on("click", function(){
	    	//var selectedTabs = $("#selectedTabs").val();
	        //$("#copied").empty();
	        $("#selectedTabsMulti option:selected").each(function(){
	           //$("#textarea").append("* "+$(this).text()+ "\n"); 
	           if($(this).text() == 'All'){
	        	   $('#selectedTabsMulti option').each(function(){
	        		   $("#selectedTabsTemp").append("<option value='"+$(this).text()+"'>"+$(this).text()+"</option> \n");
	        	   });
	        	   $("#selectedTabsMulti option").remove();
	           }else{
	           $("#selectedTabsTemp").append("<option value='"+$(this).text()+"'>"+$(this).text()+"</option> \n")
	           }
	           //$("#selectedTabsMulti option[value='"+$(this).text()+"']").remove();
	           
	           /* if(selectedTabs != '' && selectedTabs != undefined && !selectedTabs.endsWith(","))
	        	   selectedTabs +=",";
	           selectedTabs +=$(this).text(); */
	        }); 
	        $("#selectedTabsMulti option:selected").remove();
	        
	       // $("#selectedTabs").val(selectedTabs);
	       // return false;
	       
	      
          
	        /* selectedTabs +=','+("#selectedTabsTemp option:selected").val();
	        $("#selectedTabs").text(selectedTabs); */
	        //alert($("#selectedTabs").val());
	    });   
	    
	    $("#leftArrow").on("click", function(){
	        //$("#copied").empty();
	        //var selectedTabs = $("#selectedTabs").val();
	        $("#selectedTabsTemp option:selected").each(function(){
	        	if($(this).text() == 'All'){
		        	   $('#selectedTabsTemp option').each(function(){
		        		   $("#selectedTabsMulti").append("<option value='"+$(this).text()+"'>"+$(this).text()+"</option> \n");
		        	   });
		        	   $("#selectedTabsTemp option").remove();
		           }else{
		           $("#selectedTabsMulti").append("<option value='"+$(this).text()+"'>"+$(this).text()+"</option> \n")
		           }
	           //$("#selectedTabsMulti").append("<option value='"+$(this).text()+"'>"+$(this).text()+"</option> \n"); 
	           //$("#selectedTabs option[value='"+$(this).text()+"']").remove();
	        });
	        $("#selectedTabsTemp option:selected").remove();
	        
	        
	       
	    }); 
	    $("#submitted").on("click", function(){
	    	var values='';
	    	var flagSelected=$("#flagSelected").val()+"TEMP";
	    	/* if(values != '' && values != undefined){
	    		if(values.startsWith("TDG_PASSED_TABS"))
	    			values = 'TDG_PASSED_TABS';
	    		else if (values.startsWith("TDG_MASTER_TABS"))
	    			values = 'TDG_MASTER_TABS';
	    		else if (values.startsWith("TDG_REQUIRED_COLS"))
	    			values = 'TDG_REQUIRED_COLS';
	    	} */
	    	$('#selectedTabsTemp option').each(function(){
	    		if(values != '' && values != undefined && !values.endsWith(","))
	    			values +=",";
	    	values += $(this).attr("value");
	    	});
	    	$("#selectedTabs").val(values);
	    	$("#flagSelected").val(flagSelected);
	    });
	});
  
/*   function getSelectedOptions(sel, fn) {
	    var opts = [], opt;
	    
	    // loop through options in select list
	    for (var i=0, len=sel.options.length; i<len; i++) {
	        opt = sel.options[i];
	        
	        // check if selected
	        if ( opt.selected ) {
	            // add to array of option elements to return from this function
	            opts.push(opt);
	            
	            // invoke optional callback function if provided
	            if (fn) {
	                fn(opt);
	            }
	        }
	    }
	    
	    // return array containing references to selected option elements
	    return opts;
	}
  
  function callback(opt) {
	    // display in textarea for this example
	    var display = document.getElementById('selectedTabs');
	    display.innerHTML += opt.value + ', ';

	    // can access properties of opt, such as...
	    //alert( opt.value )
	    //alert( opt.text )
	    //alert( opt.form )
	} */
  </script>
</head>
<body>
     <form:form action="${pageContext.request.contextPath}/addPassedTabs" modelAttribute="tdgMasterDictionaryDTO">
          <div align="center" style="text-align: center; padding: 5%; width: 80%;">
          <form:hidden path="selectedTabs" id="selectedTabs"/>
          <form:hidden path="flagSelected" id="flagSelected"/>
            <table style="text-align: center; padding: 1%; width: 80%;">               
                <tr>
                    <td>
                        <table style="width: 80%" >
                        <thead>
                        	<tr>
							<th>Tables</th><th/><th>Selected Tables</th>
						</tr>
						</thead>
                            <tr>
                                <td align="center">
                               		<select id="selectedTabsMulti"  class="form-control-half-dictionary" multiple="multiple" size="15" style="width: 200px;">
		<c:forEach items="${tdgMasterDictionaryDTO.listTables}" var="dbConnectionsDTOs" varStatus="status">
		<option value="${dbConnectionsDTOs}">${dbConnectionsDTOs}</option>
		</c:forEach>	
		</select>
                                </td>
                                <td><input type="button" id="rightArrow" value=">>"><br><br><input type="button" id="leftArrow" value="&lt;&lt;"/>
                                </td>
                                <td align="center">
                                <select id="selectedTabsTemp" class="form-control-half-dictionary" multiple="multiple" size="15" style="width: 200px;">
                                </select>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2">
                                <input type="submit" id="submitted" class="btn-primary btn-cell" value="Submit">
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
          </div>  
      </form:form>
      <jsp:include page="ajaxfooter.jsp"></jsp:include>
      <script>
      $(document).ready(function() {
    	  $('#selectedTabsTemp option').each(function(){
    		  $("#selectedTabsMulti option[value='"+$(this).text()+"']").remove();
    	  }); 
      });
  </script> 
 </body>
</html>