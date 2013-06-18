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
          if(params[:tags])
            tags = params[:tags].split(",")

            tags.each do |tag_name|
              tag = Tag.where(:tag_text => tag_name).first

              unless(tag)
                tag = Tag.new(:tag_text => tag_name)
                tag.save
              end

              tag.ideas << @idea
            end
          end

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
    
    if @idea && @idea.users.include?(@user)
      @idea.users.delete(@user)
      
      respond_to do |format|
        format.json { render :json => @idea, :status => :ok }
      end
    end
  end
end
