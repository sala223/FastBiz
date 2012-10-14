function checkCaps(event){
	var kc = event.keyCode?event.keyCode:event.which;
	var sk = event.shiftKey ? event.shiftKey :((kc == 16)?true:false);
	if(((kc >= 65 && kc <= 90) && !sk)||((kc >= 97 && kc <= 122) && sk))
		document.getElementById('capsTip').style.visibility = 'visible';
	else
		document.getElementById('capsTip').style.visibility = 'hidden';
}