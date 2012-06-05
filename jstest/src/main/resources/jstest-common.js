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
	
	var generateUrl = function(resource, browserId, emulator) {
		var url = resource + '?browserId=' + browserId;
		if (emulator) {
			url += '&emulator=true';
		}
		return url;
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
