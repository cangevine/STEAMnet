class AuthenticationsController < ApplicationController
  
  def create
    puts request.env["omniauth.auth"]
    
    auth = request.env["omniauth.auth"]
    authentication = Authentication.find_by(provider: auth['provider'], uid: auth['uid'])
    if authentication
      user = authentication.user
    else
      user = User.create(name: auth['info']['name'], email: auth['info']['email'], password: 'test', password_confirmation: 'test')
      user.authentications.create(provider: auth['provider'], uid: auth['uid'])
    end
    
    ## Generate an access token
  end
  
end
