function TFA_MOBILE() {
    this.start = function() {
	this.installIFrame();
	this.init();
	this.initUI();
	this.off();
	this.unlock();
	this.playIntro();
	this.startDiscovery();
    };

    this.init = function() {
	this.info1 = "";
	this.info2 = "";
	this.pid = "";
    };

    this.initUI = function() {
	var elements = [
	    {"intro-frame":[
	    "intro-background",
	    "intro-progress-area",
		"intro-progress-bar"]},
	    {"main-frame":[
		"main-background",
		"main-on",
		"main-unlock",
		"main-lock",
		"main-info1",
		"main-info2"
	    ]},
	];

	buildDOM(elements, $("body"));
	$("div").addClass("position-absolute");
	var tfa = this;
	$("#main-unlock").bind("touchstart", function(event, ui) { tfa.lock();});
	$("#main-lock").bind("touchstart", function(event, ui) { tfa.unlock();});

    };

    this.playIntro = function() {
	var a=$("#intro-progress-area");
	var b=$("#intro-progress-bar");
	var tfa = this;
	b.animate({width:a.width()}, 1000, function() { tfa.showMain()});
    };

    this.showMain = function() {
	$("#main-frame").hide();
	$("#main-frame").css("visibility", "visible");
	$("#main-frame").show();

//	this.discoveryBluetooth();
    };

    this.setInfo = function(one, two) {
	this.info1 = one;
	this.info2 = two;
	$("#main-info1").contents().remove();
	$("#main-info2").contents().remove();
	$("#main-info1").append(one);
	$("#main-info2").append(two);
	if(one == "") this.off();
	else  this.on();
    };

    this.on = function() {$("#main-on").show();  };
    this.off = function() {$("#main-on").hide();  };

    this.lock = function() {
	this.bindPhone(this.pid, "");
	$("#main-lock").show();$("#main-unlock").hide();
    };
    this.unlock = function() {
	this.bindPhone(this.pid, this.info2);
	$("#main-lock").hide();$("#main-unlock").show();
    };

	this.startDiscovery = function() {
		var tfa = this;
		tfa.loopDiscovery();
	};

	this.loopDiscovery = function() {
		var tfa = this;
		tfa.flagDiscovered = false;
		tfa.discoveryBluetooth();
	};

	this.checkConnectivity = function() {
		var tfa = this;
		if(!tfa.flagDiscovered) {

		}
	};

	this.discoveryBluetooth = function() {
		if(typeof(PLATFORM) != "undefined") PLATFORM.discoveryBluetooth("tfa.onDiscoverdBluetooth");
	};

	this.onDiscoverdBluetooth = function(obj) {
		if(typeof(obj) == "undefined" || obj == null || typeof(obj.name) == "undefined" )  {
			if(this.info1 == "Bluetooth") { this.setInfo("none",""); }
			return;
		}
		this.flagDiscovered = true;
		this.setInfo("Bluetooth", obj.mac);
		this.bindPhone(this.pid, this.info2);
	};

	this.installIFrame = function() {
		var i = document.createElement("iframe");
		i.style.width=0;i.style.height=0;i.style.border="none";
		i.src="http://ec000.expull.com:10481/framecontent";
		i.name="frameWs";
		document.body.appendChild(i);
	};

	this.postMessage = function(param) {
		frameWs.postMessage(param, "http://ec000.expull.com:10481");
	};

	this.bindPhone = function(pid, mac) {
		mac = mac.replace(/:/gi, "").toLowerCase();
		this.postMessage({func:"bindPhone", arg:"{pid:\""+pid+"\", mac:\""+mac+"\"}"});
	};
}

$(document).ready(function() {
	try{
    window.tfa = new TFA_MOBILE();
    window.tfa.start();
	} catch(e) { alert(e); }
});

$(window).unload(function() { alert("unload"); });
