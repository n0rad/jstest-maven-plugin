JsTest Maven Plugin
===================

JsTest is a maven plugin to test javascript.




JsTest support in both modes :
 - multiple test framework like **jasmine, **Qunit**, or your **own** one (need to implement JsTest interface)
 - auto loading of script files or AMD support (requireJS, curl)
 - maven war overlays sources projects !
 - maven war overlays for tests dependencies !
 - nested dependencies tested in same maven reactor without package goal needed

In dev mode (dev goal to keep a running server) :
 - display tests in an html report with colors and indication of failures
 - selection of single or all tests to work on
 - display source code covered on just running tests with color and navigation (soon)
 - live refresh of test(s) to see live coding result
 - defining source of war overlays location to live coding in project but also in dependencies (soon)
 - firebug-lite. usefull for crappy browsers aka: ie (soon)

In test mode (maven normal lifecycle):
 - browser emulator using htmlunit to run tests without any browser (can be disabled)
 - plugging additional browser to tests working in different environment
 - auto start default platform browser to integrate it in build process (soon)
 - **bookmarklet** to plug an already running browser without a JsTest server currently running
 - maven console report in surefire like format (with additions as running multiple browsers)
 - **coverage report** in LCOV format
 - tests report in surefire format for all browser that run tests
 - result **and** coverage integration with the **sonar** javascript plugin 
 - result report in jenkins with a plugin (when I will have time to write one)