var app = app || {};

$(function() {
	$.fn.imagesLoaded = function( callback ){
	  var elems = this.find( 'img' ),
		  elems_src = [],
		  self = this,
		  len = elems.length;
	 
	  if ( !elems.length ) {
		callback.call( this );
		return this;
	  }
	 
	  elems.one('load error', function() {
		if ( --len === 0 ) {
		  // Rinse and repeat.
		  len = elems.length;
		  elems.one( 'load error', function() {
			if ( --len === 0 ) {
			  callback.call( self );
			}
		  }).each(function() {
			this.src = elems_src.shift();
		  });
		}
	  }).each(function() {
		elems_src.push( this.src );
		// webkit hack from http://groups.google.com/group/jquery-dev/browse_thread/thread/eee6ab7b2da50e1f
		// data uri bypasses webkit log warning (thx doug jones)
		this.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	  });
	 
	  return this;
	};
	
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

					$("#jawns").imagesLoaded(function() {
						$("#jawns").masonry({
							gutter: '.gutter-sizer',
							itemSelector: '.item'
						});
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