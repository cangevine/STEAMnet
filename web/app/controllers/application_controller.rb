class ApplicationController < ActionController::Base
  
  protect_from_forgery
  
  before_filter :verify_security_token
  
  private
    
    def verify_security_token
      # unless Digest::SHA1.hexdigest(params[:token]) == "639bab0291d34056baee0ebd1664f516ccb67efd" # Digest::SHA1.hexdigest("0577a090fea6e735f471d349b14456ea34924b00")
      #   head :unauthorized
      # end
    end
    
    def authenticate
      @user = User.find_by(name: params[:username])
      
      if(!@user)
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
