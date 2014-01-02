class ApplicationController < ActionController::Base
  
  protect_from_forgery
  
  private
    
    def authenticate
      device = Device.find_by(token: params[:token])
      
      if device
        @user = device.user
      else
        hash = { :error => "Invalid token." }
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
