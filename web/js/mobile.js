function TFA_MOBILE() {
    this.start = function() {
	this.initUI();
	this.playIntro();
    };

    this.initUI = function() {
	var elements = [
	    {"intro-frame":[
	    "intro-background",
	    "intro-progress-area",
		"intro-progress-bar"]},
	    {"main-frame":[
		"main-background",
		"main-lock",
		"main-on",
		"main-unlock",
		"main-info1",
		"main-info2"
	    ]},
	];

	buildDOM(elements, $("body"));
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
    };

}

$(document).ready(function() {
    window.tfa = new TFA_MOBILE();
    window.tfa.start();
});