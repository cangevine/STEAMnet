Rails.application.config.middleware.use OmniAuth::Builder do
  provider :developer unless Rails.env.production?
  provider :twitter, "VzXSPysXDajKDUdYzLs1Q", "KFnVDH4EU1d72YAC6SBzNzRa0qouoXrbGyPznSwhMb8"
  provider :facebook, '201337193236694', '0d30da64c231d3831ea20e5ee08763d6'
  provider :google_oauth2, "273036815840.apps.googleusercontent.com", "mExcfWtme0xAahwP9PAmJMPB"
  provider :github, "12b5ccfb3102c058db37", "69c4d3ab52bcd5546394aa00446c8145cde556e4"
  # provider :linked_in, 'CONSUMER_KEY', 'CONSUMER_SECRET'
end