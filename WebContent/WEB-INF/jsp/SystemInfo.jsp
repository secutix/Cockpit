<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <%@ include file="../../libraries.html" %>
  <title>Cockpit</title>
</head>

<script type="text/javascript">

Ext.onReady(function(response) {

	Ext.Msg.show ({
 		title		: "System Information",
 		width		: 300,
       	msg			: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<i> <b>Cockpit </b></i><br/><br/>  <b> JVM Vendor </b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <c:out value="${JVMVendor}" /> <b> <br/> JVM Version </b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${JVMVersion}"/> <br/> <b> OS Name </b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <c:out value="${OSName}"/> <br/> <b> OS Version </b> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${OSVersion}"/> <br/> <b> OS Architecture </b> : &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  <c:out value="${OSArchitecture}"/>'
	});
});


</script>
</html>