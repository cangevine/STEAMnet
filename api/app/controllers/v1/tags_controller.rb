class V1::TagsController < ApplicationController
  
  respond_to :json
  
  # GET /tags.json
  def index
    @tags = Tag.all
  end

  # GET /tags/1.json
  def show
    @tag = Tag.find_by(tag_text: params[:id])
  end
  
end
