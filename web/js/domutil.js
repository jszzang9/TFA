function buildDOM(components, parent, flagClass) {
    for(var i=0;i<components.length;i++) {
	var comp = components[i];
	if(typeof(comp) == "string") {
	    parent.append($("<div/>",(flagClass?{addClass:comp}:{id: comp})));
	}
	else {
	    var keys = Object.keys(comp);
	    for(var j=0;j<keys.length;j++) {
		var newOne = parent.append($("<div/>",((flagClass?{addClass:keys[j]}:{id: keys[j]})))).children().last();
		buildDOM(comp[keys[j]], newOne);
	    }
	}
    }
}
