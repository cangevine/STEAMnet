
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'STEAMnet' });
};

exports.sparks = function(req, res) {
	res.render('sparks', {title: 'Sparks: Ideas and inspriation'});
}

exports.projects = function(req, res) {
	res.render('projects', {title: 'Projects: The ridiculous things STEAM might actually try to do...'});
}