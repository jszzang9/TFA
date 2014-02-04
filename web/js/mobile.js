function TFA_MOBILE() {
    this.start = function() {
	this.init();
	this.initUI();
	this.playIntro();
	this.off();
	this.unlock();
    };

    this.init = function() {
	this.info1 = "";
	this.info2 = "";
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

	this.refreshInfo();
    };

    this.setInfo = function(one, two) {
	$("#main-info1").contents().remove();
	$("#main-info2").contents().remove();
	$("#main-info1").append(one);
	$("#main-info2").append(two);
    };

    this.refreshInfo = function() {
	this.setInfo(this.info1, this.info2);
    };

    this.on = function() {$("#main-on").show();  };
    this.off = function() {$("#main-on").hide();  };

    this.lock = function() {
	$("#main-lock").show();$("#main-unlock").hide();
    };
    this.unlock = function() {
	$("#main-lock").hide();$("#main-unlock").show();
    };
}

$(document).ready(function() {
    window.tfa = new TFA_MOBILE();
    window.tfa.start();
});