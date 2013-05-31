class V1::JawnsController < ApplicationController
  
  # GET /jawns
  # GET /jawns.json
  def index
    if params[:limit]
      sparks = Spark.order("id DESC").limit(params[:limit])
      ideas = Idea.order("id DESC").limit(params[:limit])
      
      @jawns = (sparks + ideas).sort_by(&:created_at).reverse.take(params[:limit].to_i) 
    else
      sparks = Spark.order("id DESC")
      ideas = Idea.order("id DESC")
      
      @jawns = (sparks + ideas).sort_by(&:created_at).reverse
    end
    
    respond_to do |format|
      format.json { render :json => @jawns }
    end
  end
  
end
