class ApplicationController < ActionController::Base
  protect_from_forgery
  
  before_filter :verify_security_token
  
  private
    
    def verify_security_token

    end
    
    def authenticate
      @user = User.find_by_name(params[:username])
      
      if(!@user)
        hash = { :error => "Invalid username." }
        respond_to do |format|
          format.json { render :json => hash, :status => :unauthorized }
        end
      end
    end
    
end
