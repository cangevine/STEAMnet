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