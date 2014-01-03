var app = app || {};

$(function() {
	Backbone.emulateHTTP = true;

	$.ajaxPrefilter( function(options, originalOptions, jqXHR) {
		options.url = 'http://steamnet.herokuapp.com/api/v1' + options.url;
	});

	var Spark = Backbone.Model.extend({
		initialize: function() {
			console.log("initializing with Spark")
		}
	});

	var Idea = Backbone.Model.extend({
		initialize: function() {
			console.log("initializing with Idea")
		}
	});

	var Jawns = Backbone.Collection.extend({
		url: '/jawns.json?lite=true',
		model: function(attrs, options) {
			if (attrs.jawn_type == "spark") {
				return new Spark(attrs, options);
			} else if (attrs.jawn_type == "idea") {
				return new Idea(attrs, options);
			}
		}
	});

	var Router = Backbone.Router.extend({
		routes: {
			"": "home"
		}
	});

	var AppView = Backbone.View.extend({
		el: '#jawns',
		render: function() {
			var jawns = new Jawns();
			jawns.fetch({
				success: function(jawns) {
					var template = _.template($("#app-view-template").html(), {jawns: jawns.models});
					this.$el.html(template);

					$("#jawns").masonry({
						gutter: '.gutter-sizer',
						itemSelector: '.item'
					});
				}.bind(this)
			});
		}
	});

	var appView = new AppView();

	var router = new Router;
	router.on('route:home', function() {
		appView.render();
	});

	Backbone.history.start();
});