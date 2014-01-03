var app = app || {};

(function() {
	var Router = Backbone.Router.extend({
		routes: {
			"": "home"
		}
	});

	app.router = new Router();

	app.router.on('route:home', function() {
		var homeview = new app.HomeView();
		homeview.render();
	});

	Backbone.history.start();
})();