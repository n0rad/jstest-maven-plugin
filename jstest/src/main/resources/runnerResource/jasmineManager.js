
var TestManager = (function() {
	"use strict";

	document.head = document.head || document.getElementsByTagName('head')[0];

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
			var ApiReport = function() {
				
			};
			ApiReport.prototype = new jasmine.JsApiReporter();
			ApiReport.prototype.reportRunnerResults = function(runner) {
				jasmine.JsApiReporter.prototype.reportRunnerResults.call(this, runner);
				xmlhttpPost(generateUrl('result/run'), new Date().getTime() - runStartTime);
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
				setInterval(function() {
					xmlhttpPost('runId', null, function(serverRunId) {
						if (serverRunId !== '' && runId !== serverRunId) {
							window.location.reload(true);
						}
					});
				}, 500);
			}
			ApiReport.prototype.reportSuiteResults = function(suite) {
				jasmine.JsApiReporter.prototype.reportSuiteResults.call(this, suite);
				var tests = [];
				for (var i = 0; i < suite.specs_.length; i++) {
					var spec = suite.specs_[i];
					tests[i] = {};
					tests[i].name = spec.description;
					
					var resultType;
					if (spec.results_.skipped) {
						resultType = 'skipped';
					} else if (spec.results_.passedCount) {
						resultType = 'success';
					} else if (spec.results_.failedCount) {
						resultType = 'failure';
					}
					tests[i].resultType = resultType;
					tests[i].duration = spec.results_.duration;
				}
				xmlhttpPost(generateUrl('result/suite'), {name : suite.description, tests : tests});
			}
			ApiReport.prototype.reportSpecStarting = function(spec) {
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