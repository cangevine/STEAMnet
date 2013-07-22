class V1::JawnsController < ApplicationController
  
  respond_to :json
  
  # GET /jawns.json
  def index
    if params[:filter] == "sparks"
      if params[:limit]
        @jawns = Spark.order("id DESC").limit(params[:limit])
      else
        @jawns = Spark.order("id DESC")
      end
    elsif params[:filter] == "ideas"
      if params[:limit]
        @jawns = Idea.order("id DESC").limit(params[:limit])
      else
        @jawns = Idea.order("id DESC")
      end
    else
      if params[:limit]
        sparks = Spark.order("id DESC").limit(params[:limit])
        ideas = Idea.order("id DESC").limit(params[:limit])

        @jawns = (sparks + ideas).sort_by(&:created_at).reverse.take(params[:limit].to_i) 
      else
        sparks = Spark.order("id DESC")
        ideas = Idea.order("id DESC")

        @jawns = (sparks + ideas).sort_by(&:created_at).reverse
      end
    end
  end
  
  def count
    @ideas = Idea.count
    @sparks = Spark.count
    
    @jawns = @ideas + @sparks
  end
  
end
