Rails.application.config.middleware.use OmniAuth::Builder do
  provider :developer unless Rails.env.production?
  # provider :twitter, 'CONSUMER_KEY', 'CONSUMER_SECRET'
  provider :facebook, '201337193236694', '0d30da64c231d3831ea20e5ee08763d6'
  # provider :linked_in, 'CONSUMER_KEY', 'CONSUMER_SECRET'
end