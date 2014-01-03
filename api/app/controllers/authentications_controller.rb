class AuthenticationsController < ApplicationController
  
  respond_to :json
  
  def index
    session[:registration_id] = params[:registration_id]
  end
  
  def create
    if params[:provider] == "developer"
      id = "testid123"
    else
      id = session[:registration_id]
    end
    
    auth = request.env["omniauth.auth"]
    
    authentication = Authentication.find_by(provider: auth['provider'], uid: auth['uid'])
    if authentication
      @user = authentication.user
      
      @new = false
    else
      @user = User.create!(name: auth['info']['name'], email: auth['info']['email'])
      @user.authentications.create(provider: auth['provider'], uid: auth['uid'])
      
      @new = true
    end
    
    @device = @user.devices.find_or_create_by(registration_id: id)
  end
  
end
