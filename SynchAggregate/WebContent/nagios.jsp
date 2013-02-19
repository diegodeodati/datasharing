<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=ISO-8859-1;no-cache">
<%@page import="java.util.List"%>
<script type="text/javascript">
function nagios(){
	var form = document.getElementById("formnagios");
	form.submit();
}
</script>


</head>
<body onload="nagios()">
	<div
		style="font-size: 14px; position: relative; width: 900px; display: block; margin-left: auto; margin-right: auto; background-color: #F2F6FC; border-radius: 20px; -webkit-border-radius: 20px; -moz-border-radius: 20px;">
		<form method="POST" action="ondemandnagios" id="formnagios"></form>
	</div>
</body>
</html>
