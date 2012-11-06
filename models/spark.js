// The Spark model

var mongoose = require('mongoose')
  , Schema = mongoose.Schema;

var sparkSchema = new Schema({
    content:  String,
    created_at: {type: Date, default: Date.now},
	updated_at: {type: Date, default: Date.now},
	external: {type: Boolean, default: false}
});

module.exports = mongoose.model('Spark', sparkSchema);