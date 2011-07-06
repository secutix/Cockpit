<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <%@ include file="../../libraries.html" %>
  <title>Cockpit</title>
</head>
<script type="text/javascript">
</script>
<body>
<c:out escapeXml='false' value="${json}"/>
</body>
</html>