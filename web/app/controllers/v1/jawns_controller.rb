class V1::JawnsController < ApplicationController
  
  respond_to :json
  
  # GET /jawns.json
  def index
    if params[:filter] == "sparks"
      @jawns = Spark.order("id DESC").offset(params[:offset]).limit(params[:limit])
    elsif params[:filter] == "ideas"
      @jawns = Idea.order("id DESC").offset(params[:offset]).limit(params[:limit])
    else
      sparks = Spark.order("id DESC").limit(params[:limit])
      ideas = Idea.order("id DESC").limit(params[:limit])
      
      @jawns = (sparks + ideas).sort_by(&:created_at).reverse
      
      if params[:offset]
        @jawns = @jawns[params[:offset].to_i..-1]
      end
      
      if params[:limit]
        @jawns = @jawns.take(params[:limit].to_i)
      end
    end
  end
  
  def count
    @ideas = Idea.count
    @sparks = Spark.count
    
    @jawns = @ideas + @sparks
  end
  
end
