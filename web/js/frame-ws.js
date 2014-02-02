var TFA_HOST="ec000.expull.com";
var TFA_PORT=10405;

function initLogArea() {
	var la = document.createElement("div");
	la.id = "logarea";
	document.body.appendChild(la);
}

function log(msg) { logarea.innerHTML += msg+"<br>"; }

function messageHaneler(message) {
	postMessage({func:"tfa.onWSMessage", arg:message});
}

function postMessage(arg) {
	parent.postMessage(arg, "http://"+TFA_HOST);
}

function openSocketWith(arg) {
	initLogArea();
	var pcid = arg.pcid;
	if(pcid == null || pcid == "") return;
	ws = new WebSocket("ws://"+TFA_HOST+":" + TFA_PORT);
	ws.onopen = function(e) { log("opened"); ws.send(pcid); }
	ws.onclose = function(e) { log("closed"); }
	ws.onmessage = function(e) { messageHaneler(e.data); }
}

function checkAuth(param) {
	var u = param.u;
	var p = param.p;
	$.post("/auth",
		{"uid":u,"pcid":p},
		function(result){
			var data;
			if(typeof(result) == "string") { eval("data = "+result); } else { data = result; }
			if("0000" == data.result) {
				postMessage({func:"tfa.hideBlock", arg:"{}"});
			}
			else {
				postMessage({func:"tfa.showBlock", arg:"{}"});
			}
		});
}

function requestAuth(param) {
	var u = param.u;
	var p = param.p;
	$.post("/auth",
		{"uid":u,"pcid":p},
		function(result){
			var data;
			if(typeof(result) == "string") { eval("data = "+result); } else { data = result; }
			if("0000" == data.result) {
				postMessage({func:"tfa.login", arg:"{}"});
			}
			else {
				postMessage({func:"tfa.failedToLogin", arg:"{}"});
			}
		});
}

function listener(event){ eval(event.data.func+"("+event.data.arg+")"); }

$(document).ready(function() {
	if (window.addEventListener) addEventListener("message", listener, false);
	else attachEvent("onmessage", listener);
	postMessage({func:"tfa.onIFrameReady", arg:"{}"});
});
