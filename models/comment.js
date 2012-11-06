// The Spark model

var mongoose = require('mongoose')
  , Schema = mongoose.Schema;

var commentSchema = new Schema({
    content:  String,
    created_at: {type: Date, default: Date.now},
	updated_at: {type: Date, default: Date.now}
});

module.exports = mongoose.model('Comment', commentSchema);