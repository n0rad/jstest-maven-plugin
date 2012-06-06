var TestManager = (function() {
	"use strict";
	
	return function(debug, isServerMode, browserId, runId, emulator) {
		
		
		var testStartTime;
		var runStartTime;
		var suiteStartTime;
		var tests = {"__0__" : []};
		
		QUnit.log(function(a) {
		});

		QUnit.testStart(function(a) {
			testStartTime = new Date().getTime();
		});

		QUnit.testDone(function(test) {
			var duration = new Date().getTime() - testStartTime;
			
			var res = {};
			res.name = test.name;
			
			if (test.module != undefined) {
				var descSplit = test.module.split(';', 2);
				if (descSplit.length === 2) {
					res.classname = descSplit[0];
				}
			}
			if (test.total == 0) {
				res.skipped = true;
			}
			res.duration = duration;
			if (test.failed > 0) {
				res.failure = {};
				res.failure.message = "no info";
			}
			if (test.module != undefined) {
				tests[test.module].push(res);
			} else {
				tests["__0__"].push(res);
			}
		});

		QUnit.moduleStart(function(a) {
			suiteStartTime = new Date().getTime();
			tests[a.name] = [];
		});

		QUnit.moduleDone(function(module) {
			var suiteResult = {};
			var descSuiteSplit = module.name.split(';', 2);
			if (descSuiteSplit.length === 2) {
				suiteResult.name = descSuiteSplit[1];
			} else {
				suiteResult.name = module.name;				
			}
			suiteResult.tests = tests[module.name];
			suiteResult.duration = new Date().getTime() - suiteStartTime;
			xmlhttpPost(generateUrl('result/suite', browserId, emulator), suiteResult);
		});

		QUnit.begin(function(a) {
			runStartTime = new Date().getTime();
		});

		QUnit.done(function(a) {
			if (tests["__0__"].length > 0) {
				var suiteResult = {};
				suiteResult.name = "UNKNOWN";
				suiteResult.tests = tests["__0__"];
				suiteResult.duration = 0;
				for (var i = 0; i < tests["__0__"].length; i++) {
					suiteResult.duration += tests["__0__"][i].duration;
				}
				xmlhttpPost(generateUrl('result/suite', browserId, emulator), suiteResult);
			}
			
			var runResult = {};
			JSCOV.storeCurrentRunResult('jasmineRun');
			runResult.coverageResult = JSCOV.getStoredRunResult()[0];
			runResult.duration = new Date().getTime() - runStartTime;
			xmlhttpPost(generateUrl('result/run', browserId, emulator), runResult);
			
			setInterval(function() {
				xmlhttpPost('runId', null, function(serverRunId) {
					if (serverRunId !== '' && runId !== serverRunId) {
						window.location.reload(true);
					}
				});
			}, 500);
		});
		
		this.run = function() {
			var body = document.getElementsByTagName('body') [0];
			var div = document.createElement('div');
			div.setAttribute("id", "qunit");
			body.appendChild(div);

		};

	};
	
	
})();