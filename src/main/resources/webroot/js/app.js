require.config({
    paths: {
	jquery: '//code.jquery.com/jquery-1.9.1.min',
	bootstrap: '//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min',
	backbone: "//cdnjs.cloudflare.com/ajax/libs/backbone.js/1.1.2/backbone-min",
	underscore:"//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.6.0/underscore-min",
	text:"//cdnjs.cloudflare.com/ajax/libs/require-text/2.0.10/text"
    },
    shim: {
	bootstrap: ['jquery'],
    }
});

require(['jquery',
	 'underscore',
	 'backbone',
	 'controller-tfa',
	],
	function ($, _, backbone, controller) {
	    controller.initialize();
	});
