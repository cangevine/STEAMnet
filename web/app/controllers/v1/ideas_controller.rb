class V1::IdeasController < ApplicationController
  
  respond_to :json
  before_filter :authenticate, :only => [:create, :destroy]
  
  # GET /ideas.json
  def index
    if params[:limit]
      @ideas = Idea.order("id DESC").limit(params[:limit])
    else
      @ideas = Idea.order("id DESC")
    end
    
    respond_with @ideas
  end
  
  # GET /ideas/1.json
  def show
    @idea = Idea.find(params[:id])
    
    respond_with @idea
  end
  
  # POST /ideas.json
  def create
    @idea = Idea.new(params[:idea])
    
    if params[:sparks]
      spark_ids = params[:sparks].split(",")
      sparks = []
    
      sparks_exist = false

      spark_ids.each do |spark_id|
        if s = Spark.find_by_id(spark_id.to_i)
          sparks_exist = true
          sparks << s
        end
      end
      
      if sparks_exist
        if @idea.save
          add_tags_to @idea

          sparks.each do |spark|
            @idea.sparks << spark
          end

          @user.ideas << @idea
        end

        respond_with @idea, :location => ["v1", @idea]
      else
        hash = { :error => "Invalid sparks." }

        respond_to do |format|
          format.json { render :json => hash, :status => :unprocessable_entity }
        end
      end
    else
      hash = { :error => "No sparks." }
      
      respond_to do |format|
        format.json { render :json => hash, :status => :unprocessable_entity }
      end
    end
  end
  
  # DELETE /ideas/1.json
  def destroy
    @idea = Idea.find(params[:id])
    
    if @idea && (@idea.user == @user)
      @idea.user = nil
      @idea.save
      
      respond_to do |format|
        format.json { render :json => @idea, :status => :ok }
      end
    end
  end
end
