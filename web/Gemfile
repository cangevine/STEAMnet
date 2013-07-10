source 'https://rubygems.org'

ruby '2.0.0'
gem 'rails', '4.0.0'

gem 'json'
gem 'faker'

gem 'jquery-rails' # TODO: figure out a way to get rid of these without heroku errrors
gem 'sass-rails', '~> 4.0.0'
gem 'uglifier', '>= 1.3.0'
gem 'coffee-rails', '~> 4.0.0'

group :development, :test do
  gem 'sqlite3'
  
  gem 'annotate'
  gem 'rspec-rails'
  gem 'fuubar'
  gem 'factory_girl_rails'
  gem 'webrat'
end

group :production do
	gem 'pg'
  gem 'rails_12factor'
end

# To use ActiveModel has_secure_password
gem 'bcrypt-ruby', '~> 3.0.0'

# To use Jbuilder templates for JSON
gem 'jbuilder'

# Use unicorn as the app server
gem 'unicorn'

# Deploy with Capistrano
# gem 'capistrano'

# To use debugger
# gem 'ruby-debug'
