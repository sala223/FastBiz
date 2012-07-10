<%@include file="include.jsp"%>
<html>
<head>
<meta name="description" content="fastbiz login page">
<title><spring:message code="page.login.title" />
</title>
<link rel="stylesheet" type="text/css" href="<spring:url value="/share/static/css/main.css"/>" media="screen">
<link rel="stylesheet" type="text/css" href="<spring:url value="/static/css/login.css"/>" media="screen">
<link rel="stylesheet" type="text/javascript" href="<spring:url value="/static/js/login.js"/>" media="screen">
</head>
<body>
	<div id="content" class="login_content">
		<div id="login_form_box">
			<form id="login" name="login" method="post" onsubmit="handleLogin();" action="./authenticate" target="_top" autocomplete="off">
				<fieldset style="display: none">
					<input type="hidden" name="callback" value="index.html">
				</fieldset>
				<div class="inputs">
					<div class="inputbox">
						<p style="margin-bottom: 5px">
							<label for="companycode"><spring:message code="page.login.form.companycode" /> </label>
						</p>
						<span><input class="glow" type="text" id="companycode" name="f_tenant" value=""> </span>
					</div>
					<div class="inputbox">
						<p style="margin-bottom: 5px">
							<label for="username"><spring:message code="page.login.form.username" /> </label>
						</p>
						<span><input class="glow" type="text" id="username" name="f_username" value=""> </span>
					</div>
					<div class="inputbox">
						<p style="margin-bottom: 5px">
							<label for="password"><spring:message code="page.login.form.password" /> </label>
						</p>
						<span><input class="glow" type="password" id="password" name="f_password" size="18" autocomplete="off" onkeypress="checkCaps(event)"> </span>
					</div>
					<div class="inputbox">
						<input class="checkbox" type="checkbox" id="rememberMe" name="rememberMe"> <span> <label style="margin-left: 2px" for="rememberMe"><spring:message
									code="page.login.form.rememberMe" /> </label> </span> <a style="margin-left: 100px" href="/forgotpassword.xhtml?"><spring:message code="page.login.form.forgetpassword" /> </a>
					</div>
					<div class="inputbox">
						<input class="loginButton" type="submit" id="Login" name="Login" value="<spring:message
								code="page.login.form.submit"/>"> <span class="error">${requestScope["SPRING_SECURITY_LAST_EXCEPTION"].message}</span>
					</div>
				</div>
			</form>
		</div>
		<div id="logo">
			<img src="<spring:url value="/share/static/img/logo.png"/>" />
		</div>
		<div style="width: 100%; position: relative; float: left; bottom: 0px; text-align: center;">
			<span><spring:message code="page.copyright" /> </span>
		</div>
	</div>
</body>
</html>