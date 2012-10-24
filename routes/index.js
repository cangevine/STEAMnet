
/*
 * GET home page.
 */

exports.index = function(req, res){
  res.render('index', { title: 'STEAMnet' });
};

exports.test = function(req, res) {
	res.render('test', {title: 'Sample STEAMnet page'});
}