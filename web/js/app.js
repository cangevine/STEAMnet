var app = app || {};

$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
	options.url = 'http://steamnet.herokuapp.com/api/v1' + options.url;
});

$(function() {

});