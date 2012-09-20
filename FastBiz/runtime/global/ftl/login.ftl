<#import "spring.ftl" as spring />
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="description" content="<@spring.message "page.login.title"/>"> 
<title><@spring.message "page.login.title"/>
</title>
<link rel="stylesheet" type="text/css" href="/share/static/css/main.css" media="screen">
<link rel="stylesheet" type="text/css" href="/share/static/css/login.css" media="screen">
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
						<p  class="label">
							<label for="companycode"><@spring.message "page.login.form.companycode"/></label>
						</p>
						<input class="glow" type="text" id="companycode" name="f_tenant" value="${RequestParameters.f_tenant?if_exists}">
					</div>
					<div class="inputbox">
						<p class="label">
							<label for="username"><@spring.message "page.login.form.username"/></label>
						</p>
						<input class="glow" type="text" id="username" name="f_username" value="${RequestParameters.f_username?if_exists}">
					</div>
					<div class="inputbox">
						<p class="label">
							<label for="password"><@spring.message "page.login.form.password"/></label>
						</p>
						<input class="glow" type="password" id="password" name="f_password" size="18" autocomplete="off" onkeypress="checkCaps(event)">
					</div>
					<div class="inputbox">
						<input class="checkbox" type="checkbox" id="rememberMe" name="rememberMe"> 
						<span> <label class="label" for="rememberMe"><@spring.message "page.login.form.rememberMe"/></label> </span> 
						<a style="padding-left: 20px" href="/forgotpassword.xhtml?"><@spring.message "page.login.form.forgetpassword" /> </a>
					</div>
					<div class="inputbox">
						<input class="loginButton" type="submit" id="Login" name="Login" value="<@spring.message "page.login.form.submit"/>"> <span class="error"><#if Request['SPRING_SECURITY_LAST_EXCEPTION'] ??> ${Request['SPRING_SECURITY_LAST_EXCEPTION'].message}</#if></span>
					</div>
				</div>
			</form>
		</div>
		<div id="logo">
			<img src="/share/static/images/logo.png" />
		</div>
		<div id="footer" class="footer">
			<span><@spring.message "page.copyright" /> </span>
		</div>
	</div>
</body>
</html>