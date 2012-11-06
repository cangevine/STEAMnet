// The Project model

var mongoose = require('mongoose')
  , Schema = mongoose.Schema;

var projectSchema = new Schema({
    title:  String,
	description: String,
    created_at: {type: Date, default: Date.now},
	updated_at: {type: Date, default: Date.now},
    members: {type: String, default: 'Anon'}
});

module.exports = mongoose.model('Project', projectSchema);