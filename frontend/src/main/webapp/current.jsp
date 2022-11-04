<!DOCTYPE html>

<html>
<head>

</head>

<body>
<script type="text/javascript">
var xhttp = new XMLHttpRequest();
xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
       // Typical action to be performed when the document is ready:
       document.getElementById("demo").innerHTML = xhttp.responseText;
    }
};
xhttp.open("GET", "/currentData", true);
xhttp.send();
</script>

<div id="demo">
</div>

</body>
</html>
