
var TestManager = (function() {
	"use strict";

	function getParameterByName(name) {
	  name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	  var regexS = "[\\?&]" + name + "=([^&#]*)";
	  var regex = new RegExp(regexS);
	  var results = regex.exec(window.location.search);
	  if(results == null)
	    return "";
	  else
	    return decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	
	function xmlhttpPost(strURL, obj, callback) {
	    var xmlHttpReq = false;
	    // Mozilla/Safari
	    if (window.XMLHttpRequest) {
	        xmlHttpReq = new XMLHttpRequest();
	    }
	    // IE
	    else if (window.ActiveXObject) {
	        xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
	    }
	    xmlHttpReq.open('POST', strURL, true);
	    xmlHttpReq.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
	    xmlHttpReq.onreadystatechange = function() {
	        if (xmlHttpReq.readyState == 4) {
	        	if (callback != undefined) {
	        		callback(xmlHttpReq.responseText);
	        	}
	        }
	    }
	    xmlHttpReq.send(JSON.stringify(obj));
	}
	
	function changeFavicon(src) {
		var link = document.createElement('link'), oldLink = document
				.getElementById('dynamic-favicon');
		link.id = 'dynamic-favicon';
		link.rel = 'shortcut icon';
		link.href = src;
		if (oldLink) {
			document.head.removeChild(oldLink);
		}
		document.head.appendChild(link);
	}


	return function(debug, isServerMode, browserId, runId, emulator) {
		var serverMode = isServerMode;
		
		var generateUrl = function(resource) {
			var url = resource + '?browserId=' + browserId;
			if (emulator) {
				url += '&emulator=true';
			}
			return url;
		}
		
		var buildFailureDesc = function(jasmineExpectResult) {
			var failDesc = {};
			failDesc.message = jasmineExpectResult.message;
			failDesc.expected = jasmineExpectResult.expected;
			failDesc.actual = jasmineExpectResult.actual;
			failDesc.type = jasmineExpectResult.matcherName;
			failDesc.value = jasmineExpectResult.trace.stack;
			if (failDesc.value == undefined) {
				failDesc.value = failDesc.message;
			}
			return failDesc;
		}
		
		var buildTestResult = function(spec) {
			var test = {};
			test.name = spec.description;
			test.classname = spec.testFile;
			if (spec.results_.skipped) {
				test.skipped = true;
			}
			test.duration = spec.results_.duration;	
			if (spec.results_.failedCount) {
				test.failure = buildFailureDesc(spec.results_.items_[0]);
			}			
			return test;
		}

		this.run = function() {

			var HtmlReport = function() {

			};
			HtmlReport.prototype = new jasmine.TrivialReporter();
			HtmlReport.prototype.reportRunnerResults = function(runner) {
				jasmine.TrivialReporter.prototype.reportRunnerResults
						.call(this, runner);
				var results = runner.results();
				var newFavicon = (results.failedCount > 0) ? "favicon-fail.ico"
						: "favicon-success.ico";
				changeFavicon(newFavicon);
			}
			HtmlReport.prototype.reportSpecResults = function(spec) {
				jasmine.TrivialReporter.prototype.reportSpecResults.call(this, spec);
			}
			HtmlReport.prototype.reportSuiteResults = function(suite) {
				jasmine.TrivialReporter.prototype.reportSuiteResults.call(this, suite);
			}
			HtmlReport.prototype.reportSpecStarting = function(spec) {
				jasmine.TrivialReporter.prototype.reportSpecStarting.call(this, spec);
			}

			
			var testStartTime;
			var runStartTime;
			var suiteStartTime;
			var ApiReport = function() {
				
			};
			ApiReport.prototype = new jasmine.JsApiReporter();
			
			ApiReport.prototype.reportRunnerResults = function(runner) {
				jasmine.JsApiReporter.prototype.reportRunnerResults.call(this, runner);
				var runResult = {};
				JSCOV.storeCurrentRunResult('jasmineRun');
				runResult.coverageResult = JSCOV.getStoredRunResult()[0];
				runResult.duration = new Date().getTime() - runStartTime;
				xmlhttpPost(generateUrl('result/run'), runResult);
				
				setInterval(function() {
					xmlhttpPost('runId', null, function(serverRunId) {
						if (serverRunId !== '' && runId !== serverRunId) {
							window.location.reload(true);
						}
					});
				}, 500);
			}
			
			ApiReport.prototype.reportSpecResults = function(spec) {
				var duration = new Date().getTime() - testStartTime;
				spec.results_.duration = duration;
				jasmine.JsApiReporter.prototype.reportSpecResults.call(this, spec);
				if (!debug) {
					return;
				}
				var resultType;
				if (spec.results_.skipped) {
					resultType = 'skipped';
				} else if (spec.results_.passedCount) {
					resultType = 'success';
				} else if (spec.results_.failedCount) {
					resultType = 'failure';
				}
				xmlhttpPost(generateUrl('result/test'), {name : spec.description,
											resultType : resultType,
											duration : duration});
			}
			
			ApiReport.prototype.reportRunnerStarting = function(runner) {
				jasmine.JsApiReporter.prototype.reportRunnerStarting.call(this, runner);
				runStartTime = new Date().getTime();
				suiteStartTime = runStartTime;
			}
			
			ApiReport.prototype.reportSuiteResults = function(suite) {
				jasmine.JsApiReporter.prototype.reportSuiteResults.call(this, suite);
				var tests = [];
				for (var i = 0; i < suite.specs_.length; i++) {
					tests[i] = buildTestResult(suite.specs_[i]);
				}
				if (suite.parentSuite == null) {
					var descSuiteSplit = suite.description.split(';', 2);
					if (descSuiteSplit.length === 2) {
						suite.description = descSuiteSplit[1];
					}
				}

				
				var suiteResult = {};
				suiteResult.name = suite.description;
				suiteResult.tests = tests;
				suiteResult.duration = new Date().getTime() - suiteStartTime;
				xmlhttpPost(generateUrl('result/suite'), suiteResult);
				suiteStartTime = new Date().getTime();
			}
			
			ApiReport.prototype.reportSpecStarting = function(spec) {
				var rootSuite = spec.suite;
				while (rootSuite.parentSuite !== null) {
					rootSuite = rootSuite.parentSuite;
				}
				var descSplit = rootSuite.description.split(';', 2);
				if (descSplit.length === 2) {
					spec.testFile = descSplit[0];
				}
				testStartTime = new Date().getTime();
			}

			if (!serverMode) {
				window.reporter = new ApiReport();
			} else {
				window.reporter = new HtmlReport();
			}
			jasmine.getEnv().addReporter(reporter);
			jasmine.getEnv().execute();
		};

	};

})();