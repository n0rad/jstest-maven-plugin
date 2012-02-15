<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>JsTest Runner</title>
	$cssDependencies$
	$javascriptDependencies$
	<script type="text/javascript" src="$requirejsPath$"></script>
</head>
<body>
	<script type="text/javascript">

		var specs = $specs$;

		require.config({
		    baseUrl: '$sourceDir$'
		    $if(priority)$
		    , priority: $priority$
		    $endif$
		    $if(customRunnerConfiguration)$
		    , $customRunnerConfiguration$
		    $endif$
        });

		require(specs, function() {
		    window.reporter = new jasmine.$reporter$(); jasmine.getEnv().addReporter(reporter);
			jasmine.getEnv().execute();
		});
	</script>
</body>
</html>