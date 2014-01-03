var app = app || {};

(function() {
	app.Jawns = Backbone.Collection.extend({
		url: '/jawns.json?lite=true',
		model: function(attrs, options) {
			if (attrs.jawn_type == "spark") {
				return new app.Spark(attrs, options);
			} else if (attrs.jawn_type == "idea") {
				return new app.Idea(attrs, options);
			}
		}
	});
})();