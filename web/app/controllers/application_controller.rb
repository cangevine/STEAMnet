class ApplicationController < ActionController::Base
  
  protect_from_forgery
  
  # before_action :authenticate
  
  private
    
    def authenticate_new
      device = Device.find_by(token: params[:token])
      
      head :unauthorized unless device
      
      @user = device.user
    end
    
    def authenticate
      @user = User.find_by(name: params[:username])
      
      unless @user
        hash = { :error => "Invalid username." }
        respond_to do |format|
          format.json { render :json => hash, :status => :unauthorized }
        end
      end
    end
    
    def add_tags_to(jawn)
      if(params[:tags])
        tags = params[:tags].split(",")

        tags.each do |tag_name|
          tag = Tag.find_or_create_by(tag_text: tag_name)

          jawn.tags << tag
        end
      end
    end
    
end
