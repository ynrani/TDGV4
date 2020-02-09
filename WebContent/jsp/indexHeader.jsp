<%@taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>
<section class="headerDiv">
	<header>
		<section class="top-blue-nav">
			<div class="primary-nav">
				<nav>
					<ul>
						<li><a href="./index"><img src="images/home-icon.png"
								width="20" height="20" alt="" /> Home</a></li>
						<li><a href="http://www.capgemini.com/about-capgemini"
							TARGET="_NEW"><img src="images/about-icon.png" width="20"
								height="20" alt="" /> About Us</a></li>
						<li><a href="http://www.in.capgemini.com/contact-capgemini"
							TARGET="_NEW"><img src="images/contact-icon.png" width="20"
								height="20" alt="" /> Contact Us</a></li>
						<li><a href="./logout?logout=true"><img
								src="images/logout-icon.png" width="20" height="20" alt="" />Logout</a></li>
					</ul>
				</nav>
			</div>
			<div class="welcome" >
			<marquee>
				<h5>
					Welcome
					<%
					out.println((String) session.getAttribute("UserId"));
				%>
				</h5>
				</marquee>
			</div>
		</section>
		<section class="navigation">
			<div class="logo">
				<img src="images/logo-cap.jpg" class="logo" alt="" />
			</div>
			
			
			
			
			<div class="main-nav">
				<div id="cssmenu">
					<ul>
					<security:authorize access="hasRole('ROLE_ADMIN')">
						<li id="admin"><a href="#" >Admin</a>
							<ul>
							<li id="admin_db_connection"><a href="#" >Database Connections</a>
									<ul>
										<li id="db_connection"><a href="./dataConAddConnection">Add Connections</a></li>
										<li id="db_connection_list"><a href="./dataConListConnections">Modify/Remove Connections</a></li>
										
									</ul>
								</li>
								<li id="admin_user_mgmt"><a href="#">User Management</a>
									<ul>
										<li id="admin_user_mgmt_add"><a
											href="./tesdaCreateNewUser">Add Users</a></li>
										<li id="admin_user_mgmt_modify"><a href="./testdaAdmin">Modify
												Users</a></li>
										<li id="admin_user_mgmt_remov"><a href="./testdaAdmin">Remove
												Users</a></li>
										<li id="admin_user_mgmt_chage"><a href="#">Change Password</a></li>
									</ul></li>
								<li><a href="#">Application Configuration Management</a>
									<ul>
										
										<li><a href="#">Master Dictionary</a>
											<ul>
												<li><a href="./tdgaCreateMasterDictionary">Create Master Dictionary</a></li>
												<li><a href="./tdgMasterDictionaryDashboard">Update Master	Dictionary </a></li>
												<li><a href="./tesdaMasterDictionary">Upload Master	Dictionary</a></li>
												<li><a href="./tdgUploadmtcFiles">Master Dictionary Table Creation</a></li>
											</ul>
										</li>
										<li><a href="#">Manual Dictionary</a>
											<ul>
												<li><a href="./tdgaManualDictionaryDashboard">Manual Dictionary Dashboard</a></li>
												<li><a href="./tdgUploadmdFiles">Upload Manual Dictionary</a></li>											
											</ul>
										</li>
											
											<li><a href="#">Table Alias</a>
												<ul>
													<li><a href="./createalias">Add Table Alias</a></li>
													<li><a href="./aliasdashboard">Table Alias Dashboard</a></li>
													<li><a href="./tdgUploadFiles">Upload Table Aliases</a></li>
												</ul>
										</li>
										<!-- <li><a href="./tdgUploadFiles">Add Files</a></li>
										<li><a href="./tdgaCreateMasterDictionary">Create Master Dictionary</a></li>
										<li><a href="./tdgaManualDictionaryDashboard">Manual Dictionary Dashboard</a></li>
										<li><a href="./tesdaMasterDictionary">Upload Master	Dictionary</a></li>
										<li><a href="./tdgMasterDictionaryDashboard">Update Master	Dictionary </a></li>
										<li><a href="#">Table Alias Operations</a>
											<ul>
												<li><a href="./createalias">Add Table Alias</a></li>
												<li><a href="./aliasdashboard">Table Alias Dashboard</a></li>
											</ul>
										</li> -->
									</ul>
								</li>
							</ul></li>
							</security:authorize>
						<li id="services"><a href="#">Services</a>
							<ul>
								<li><a href="./tdgOperationsDetails">Test Data Generation</a></li>
								<li><a href="./tdgDashBoardDetails">DashBoard</a></li>
								
								<li id="metrics"><a href="#">Metrics</a>
									<ul>
										<li id="admin_user_mgmt_add"><a
											href="./metricsDetails">Metrics Dashboard</a></li>
										<li id="livechart"><a href="./metricslive">Live Chart</a></li>
									</ul></li>
									
									
								<!-- <li><a href="./metricsDetails">Metrics</a></li> -->
								<!-- <li><a href="./tdgDataConditional">Data Conditioner</a></li>
								<li><a href="./tdgDataConditionalDashboard">Data
										Conditioner DashBoard</a></li> -->
							</ul></li>
						<li id="train"><a href="#">Training</a>
							<ul>
								<li id="train_lear"><a href="#">Manuals</a></li>
								<li id="train_vid"><a href="#">Videos</a></li>
							</ul></li>
					</ul>
				</div>
			</div>
			
			<div align = "right" >
				<img src="images/ANZ-logo.jpeg"  alt="" />
			</div>
			
		</section>
		<section class="title-band">
			<div class="title">
				<h3 class="h3Tdm">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h3>
			</div>
		</section>
	</header>
</section>