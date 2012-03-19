<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>JsTest Runner</title>
	$testResources$

	$sources$

	$tests$
</head>
<body>
	<script type="text/javascript">
		
		var testManager = new TestManager($debug$, $serverMode$, $browserId$, "$runId$", $emulator$);
		
		if (window.addEventListener) {
			addEventListener('DOMContentLoaded', testManager.run, false);
		} else {
			attachEvent('onload', testManager.run);
		}		
	
	</script>
</body>
</html>