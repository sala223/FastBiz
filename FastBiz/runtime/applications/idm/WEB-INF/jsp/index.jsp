<div id="login_form_box">
 <%out.println("test")%>
	<form id="login" name="login" method="post" onsubmit="handleLogin();" action="" target="_top" autocomplete="off">
		<fieldset style="display: none">
			<input type="hidden" name="callback" value="index.html">
		</fieldset>
		<div class="inputs">
			<div class="inputbox">
				<p>
					<label for="username">User Name</label>
				</p>
				<span><input class="txtbox glow" type="text" id="username" name="username" value=""> </span>
			</div>
			<div class="inputbox">
				<p>
					<label for="password">Password</label>
				</p>
				<span><input class="txtbox glow" type="password" id="password" name="pw" size="18" autocomplete="off" onkeypress="checkCaps(event)"> </span>
			</div>
			<div id="pwcaps" class="pwcaps" style="display: none">
				<img src="/login/assets/warning16.png" alt="打开了 Caps Lock！"> 打开了 Caps Lock！
			</div>
			<div class="inputbox">
				<input class="checkbox" type="checkbox" id="rememberUn" name="rememberUn"><label for="rememberUn">记住用户名</label>
			</div>
			<div class="inputbox">
				<input class="loginButton" type="submit" id="Login" name="Login" value="登录"><a class="forgotpass" href="/secur/forgotpassword.jsp?locale=cn">忘记了密码？</a>
			</div>
		</div>
		<div class="hidesubmit">
			<input type="image" src="/login/assets/trans.gif" alt="Submit" value="Login">
		</div>
	</form>
	<div id="signupbox">
		没有帐户？<a class="signup_link_button" href="https://www.salesforce.com/cn/form/trial/freetrial.jsp?d=70130000000EndO">免费注册。</a>
	</div>
</div>