package com.tesda.util;

public class AppConstant
{


	public static final String ORA_DRIVER = "oracle.jdbc.OracleDriver";
	public static final String SQL_SERVER_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	public static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";

	public static final String ORA_URL = "jdbc:oracle:thin:@";
	public static final String SQL_SERVER_URL = "jdbc:jtds:sqlserver://";
	public static final String MYSQL_URL = "jdbc:mysql://";
	public static final String DB2_URL = "jdbc:db2://";

	public static final String INVALID_UNAME_PASS = "Invalid username and password!";
	public static final String LOGOUT_SUCCESS = "You've been logged out successfully.";
	public static final String SESSION_EXPIRED = "You are not allowed to perform 'Back' or You have not logged in or Session Expired.";

	
	public static final String ERRORS = "errors";
	public static final String BUTTON = "Button";
	public static final String FAILED = "FAILED";
	public static final String SUCCESS = "SUCCESS";
	public static final String SUCCESS_SMALL = "Success";
	public static final String TDM_ADMIN_DISPLAY_USER = "displayUser";

	

	public static final String DB_CON = "/dataConAddConnection";
	public static final String DB_CON_VIEW = "dataConAddConnection";

	public static final String DB_CON_LIST = "/dataConListConnections";
	public static final String DB_CON_LIST_VIEW = "dataConListConnections";
	public static final String DB_CON_DELETE = "dataConDeleteConnection";

	public static final String DB_CON_LIST_RED = "redirect:dataConListConnections";

	public static final String TEST_CON = "testCon";
	public static final String CREATE_CON = "create";
	public static final String BTN = "btn";
	public static final String SAVE_STS = "saveStatus";

	public static final String DB_CONN_DTLS = "dbConnectionsDTOs";

	public static final String DB_TYPE_ORACLE = "Oracle";
	public static final String DB_TYPE_MYSQL = "MySQL";
	public static final String DB_TYPE_DB2 = "DB2";
	public static final String DB_TYPE_SQLSERVER = "SqlServer";
	public static final String SQLSERVER = "SQLSERVER";
	public static final String GET_TABLES = "GET_TABLES";
	public static final String GET_ALL_TABLES = "GET_ALL_TABLES";
	public static final String GET_SEQUENCE_TABLES = "GET_SEQUENCE_TABLES";

	public static final String CLASS_NAME = "DataCon{0}Constant";

	public static final String ORACLE = "ORACLE";
	public static final String MYSQL = "MYSQL";
	public static final String DB2 = "DB2";
	
	public static final String SUCCESS_SAVE_MSG = "Successfully Saved...";
	public static final String SAVE_FAIL_MSG = "Save Failed...!";
	public static final String SUCCESS_PING_MSG = "Ping Successfull...!";
	public static final String PING_FAIL_MSG = "Ping Failed...!";
	public static final String STATUS = "status";
	
	public static final String SESSION_UID = "UserId";

}
