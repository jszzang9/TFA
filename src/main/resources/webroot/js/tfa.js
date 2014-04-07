var controller = {};

controller.request = function(url, context, callback) {
    $.post(url, context, callback);
}

controller.loadMaster = function(callback) {
    $.get("/loadMaster", '', function(raw) {
	$(".tbody-master").contents().remove();
	eval("var data="+raw);
	_.each(data.management.list, function(item) {
	    var safe = _.defaults(item, {userid:"", auth:"", exception:"", approval:""});
	    var approval = safe.approval == ""
		? ""
		: new Date(parseFloat(safe.approval)).toLocaleString();
	    var tr = $(""
		       +"<tr>"
		       +"<td>"
		       +"<span class='status'>"+(item.status=="connected"?"O":"")+"</span>"
		       +"<button class='btn btn-primary btn-check-auth'>인증테스트</button>"
		       +"</td>"
		       +"<td>"+safe.token+"</td>"
		       +"<td>"+safe.userid+"</td>"
		       +"<td>"+safe.contact+"</td>"
		       +"<td>"+safe.auth+"</td>"
		       +"<td>"+safe.exception+"</td>"
		       +"<td>"+approval+"</td>"
		       +"<td class='controller'></td>"
		       +"<//tr>"
		      );
	    (function(tr, safe) {
		tr.find("button").click(function() {
		    controller.request("/auth", {uid:safe.userid,pcid:123}, function(raw) {
			eval("var data = "+raw);
			tr.find(".status").html((data.result == "connected" ? "O":"X"));
		    });
		});
	    })(tr, safe);
	    $(".tbody-master").append(tr);
	}); // each
	callback();
    });
}

controller.initListeners = function() {
    $(".btn-save-master").click(function() {
	var token = $(".master-new-token-value").val();
	var contact = $(".master-new-contact-value").val();
	controller.request("/createMaster", {token:token, contact:contact},
			   function() {
			       controller.loadMaster(function() {
				   $(".master-new-token-value").val("");
				   $(".master-new-contact-value").val("");
				   $("#modal-save-master").modal("hide");
			       });
			   });
    });
}
controller.initMasterModal = function(obj, callback) {
    
}

$(document).ready(
    function() {
	controller.initListeners();
	controller.loadMaster(function() {});
    } 
);
