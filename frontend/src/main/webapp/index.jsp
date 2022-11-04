<!DOCTYPE html>
<%@page import="java.util.Properties" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.github.andy.elva.model.frontend.*" %>
<html>
<head>
<script src="//cdnjs.cloudflare.com/ajax/libs/dygraph/2.1.0/dygraph.min.js"></script>
<link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/dygraph/2.1.0/dygraph.min.css" />

<script type="text/javascript">
window.onload = function() {
    document.getElementById("cpower").checked = true;
    document.getElementById("cel").checked = true;
    document.getElementById("cva").checked = true;
    update();
  }
</script>


<script type="text/javascript">
 function update() {
     var e = document.getElementById("graphType");
     var currentValue = e.options[e.selectedIndex].text;
     g = new Dygraph(document.getElementById("graphdiv"),
     "/data?timeUnit="+currentValue+
     "&power="+document.getElementById("cpower").checked+
     "&el="+document.getElementById("cel").checked+
     "&va="+document.getElementById("cva").checked+
     "&quantity="+document.getElementById("quantity").value+
      "&historyDays="+document.getElementById("historyDays").value
     ,
     {
      connectSeparatedPoints : true

      });
 }
</script>
   <style type="text/css">
    #graphdiv {
      position: absolute;
      left: 10px;
      right: 10px;
      top: 80px;
      bottom: 10px;
    }
    </style>
</head>

<body>
<div style="margin-bottom:20px;">
<a href="current.jsp">Current power</a>
Show last <input value="7" type="number" id="historyDays" name="historyDays" min="0" max="30000"  onChange="update()">
days with sliding average
 <input value="1" type="number" id="quantity" name="quantity" min="0" max="30000"  onChange="update()">




<select name="graphType" id="graphType"  onChange="update()">
  <option value="Minutes">Minutes</option>
  <option value="Hours">Hours</option>
  <option selected value="Days">Days</option>
  <option value="Weeks">WEEKS</option>
  <option value="Months">Months</option>
</select>

 <input type="checkbox" id="cpower" name="power" value="el" onChange="update()"><label for="power">power</label>
 <input type="checkbox" id="cel" name="el" value="el" onChange="update()"><label for="el">el</label>
 <input type="checkbox" id="cva" name="va" value="va" onChange="update()"><label for="va">va</label>
</div>
<hr>

	<div id="graphdiv"></div>
</body>
</html>
</html>
