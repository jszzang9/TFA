define([], function() {
    this.initialize = function() {
	new (Backbone.View.extend({
	    events: {
		"click .btn-lg":"login",
	    },
	    login:function() {
		var id = $(".login-id").val();
		var pw = $(".login-pw").val();
		if(id == "admin" && pw == "asdf") {
		    document.location="main.html";
		}
		else {
		    alert("Invalid ID or Password");
		    $(".login-val").val("");
		}
	    }
	}))({el:"body"});

	displayTemplate($("body"), "signin", {}, function() {});
    }

    var displayTemplate = function(parent, page, context, callback) {
	require(['text!templates/'+page+'.html'], function(p) {
	    var a=_.template(p, context);
	    parent.html(a);
	    callback(parent);
	});
    }

    return {
	initialize:this.initialize
    };
})