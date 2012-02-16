var TestManager = (function() {
	"use strict";

	document.head = document.head || document.getElementsByTagName('head')[0];

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


	return function(isServerMode) {
		this.serverMode = isServerMode;

		this.run = function() {

			var Report = function() {

			};
			Report.prototype = new jasmine.TrivialReporter();
			Report.prototype.reportRunnerResults = function(runner) {
				jasmine.TrivialReporter.prototype.reportRunnerResults
						.call(this, runner);
				var results = runner.results();
				var newFavicon = (results.failedCount > 0) ? "favicon-fail.ico"
						: "favicon-success.ico";
				changeFavicon(newFavicon);
			}

			
			
			
			if (!this.serverMode) {
				window.reporter = new jasmine.JsApiReporter();
			} else {
				window.reporter = new Report();
			}
			jasmine.getEnv().addReporter(reporter);
			jasmine.getEnv().execute();
		};

	};

})();