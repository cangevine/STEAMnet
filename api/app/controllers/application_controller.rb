class ApplicationController < ActionController::Base
  
  before_filter :allow_cross_domain

  protect_from_forgery

  def options
    allow_cross_domain
    render :text => "", :layout => false
  end
  
  private

    def allow_cross_domain
      headers["Access-Control-Allow-Origin"] = "*";
      headers["Access-Control-Request-Method"] = "*";
      headers["Access-Control-Allow-Methods"] = "PUT, OPTIONS, GET, DELETE, POST"
      headers['Access-Control-Allow-Headers'] = '*,x-requested-with,Content-Type'
    end
    
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
