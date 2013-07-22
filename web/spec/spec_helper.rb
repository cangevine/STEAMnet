require 'paperclip/matchers'

# This file is copied to spec/ when you run 'rails generate rspec:install'
ENV["RAILS_ENV"] ||= 'test'
require File.expand_path("../../config/environment", __FILE__)
require 'rspec/rails'

# Requires supporting ruby files with custom matchers and macros, etc,
# in spec/support/ and its subdirectories.
Dir[Rails.root.join("spec/support/**/*.rb")].each {|f| require f}

RSpec.configure do |config|
  config.include Paperclip::Shoulda::Matchers
  
  # == Mock Framework
  #
  # If you prefer to use mocha, flexmock or RR, uncomment the appropriate line:
  #
  # config.mock_with :mocha
  # config.mock_with :flexmock
  # config.mock_with :rr
  config.mock_with :rspec
  config.before do
    @test_user = FactoryGirl.create(:user)
    @auth_token = @test_user.devices.create(registration_id: "testid123").token
  end

  # If you're not using ActiveRecord, or you'd prefer not to run each of your
  # examples within a transaction, remove the following line or assign false
  # instead of true.
  config.use_transactional_fixtures = true
  
  config.render_views = true
end

OmniAuth.config.test_mode = true
OmniAuth.config.mock_auth[:developer] = {
  'uid' => 'my@email.com',
  'provider' => 'developer',
  'info' => {
    'name' => 'Test User',
    'email' => 'my@email.com'
  }
}