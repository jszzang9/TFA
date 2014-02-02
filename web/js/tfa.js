function TFA_WEB() {
	this.loadSaved = function() {
		this.pcid = localStorage.getItem("pcid");
		this.uid = localStorage.getItem("uid");
		try { $("#txtPCID")[0].value=this.pcid;} catch(e) {}
	};

	this.start = function() {
		this.loadSaved();
		this.installAlertDiv();
		this.installIFrame();
		this.installBlock();
		this.sessionRequired = true;
	};

	this.updatePCID = function() {
		localStorage.setItem("pcid", $("#txtPCID")[0].value);
	};

	this.checkLoginForm = function() {
		var mustfilled = [txtUID, txtPW, txtPCID];
		for(var i=0;i<mustfilled.length;i++) {
			if(mustfilled[i].value == "") {
				this.alert("Please, fill all of required input");
				mustfilled[i].focus();
				return false;
			}
		}
		return true;
	};

	this.alert = function(message)  {
		var fadeInTerm = 1000;
		var fadeOutTerm = 1000;
		$("#divAlert").contents().remove();
		$("#divAlert").append(message);
		$("#divAlert").fadeIn(fadeInTerm, function() {$("#divAlert").fadeOut(fadeOutTerm);});
	};

	this.showBlock = function() { if(!this.sessionRequired) return; $("#divBlockFrame").fadeIn(1000); };
	this.hideBlock = function() { $("#divBlockFrame").fadeOut(1000); };
	this.detectPhone = function(arg) {
		this.alert("["+arg.pid+"] is detected.");
		this.checkAuth();
	};
	this.lostPhone = function(arg) {
		this.alert("["+arg.pid+"] is lost.");
		this.checkAuth();
	};
	this.expireSession = function(arg) {
		this.logout();
	};


	this.checkAuth= function() {
		this.postMessage( {func:"checkAuth", arg:"{u:'"+this.uid+"',p:'"+this.pcid+"'}"});
	};	


	this.requestAuth = function() {
		if(!this.checkLoginForm()) return;
		this.uid = txtUID.value;
		this.pcid = txtPCID.value;
		localStorage.setItem("uid", this.uid);
		this.postMessage( {func:"requestAuth", arg:"{u:'"+this.uid+"',p:'"+this.pcid+"'}"});
	};	

	this.postMessage = function(param) {
		frameWs.postMessage(param, "http://ec000.expull.com:10481");
	};

	this.login = function() { document.location = "tfa-loggedin.html" };
	this.logout = function() { document.location = "index.html" };
	this.failedToLogin= function() { this.alert("Failed to login"); };

	this.installIFrame = function() {
		var i = document.createElement("iframe");
		i.style.width=0;i.style.height=0;
		i.src="http://ec000.expull.com:10481/framecontent";
		i.name="frameWs";
		document.body.appendChild(i);
	};

	this.installAlertDiv = function() {
		var a = document.createElement("div");
		a.style.position="absolute";
		a.style.width="100%";
		a.className = "alert alert-danger";
		a.id = "divAlert";
		document.body.insertBefore(a, document.body.firstChild);
		$("#divAlert").hide();
	};

	this.installBlock = function() {
		var a = document.createElement("div");
		a.className = "blockframe";
		a.id = "divBlockFrame";
		document.body.appendChild(a);
		$(a).hide();
	};

	this.onIFrameReady = function() {
		this.postMessage({func : "openSocketWith", arg:"{pcid:\""+this.pcid+"\"}"});
	};

	this.onWSMessage = function(message) {
		var func = message.func;
		eval("this."+func+"("+message.arg+")");
	};
}

$(document).ready(function() {
	window.tfa = new TFA_WEB()
	tfa.start();
});

function listener(event){ eval(event.data.func+"("+event.data.arg+")"); }
if (window.addEventListener) addEventListener("message", listener, false);
else attachEvent("onmessage", listener);

