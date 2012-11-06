var Spark = require("../models/spark.js");
var Project = require("../models/project.js");

exports.index = function(req, res){
  res.render('index', { title: 'STEAMnet' });
};

exports.sparks = function(req, res) {
	return Spark.find(function(error, data) {
		return res.render('sparks', {title: 'Sparks: Ideas and inspriation', sparks: data});
	});
}

exports.projects = function(req, res) {
	res.render('projects', {title: 'Projects: The ridiculous things STEAM might actually try to do...'});
}

exports.threads = function(req, res) {
	res.render('threads', {title: 'Threads: The conversations happening now on STEAMnet'})
}