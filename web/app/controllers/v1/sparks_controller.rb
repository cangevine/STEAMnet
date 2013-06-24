class V1::SparksController < ApplicationController
  
  respond_to :json
  before_filter :authenticate, :only => [:create, :destroy]
  
  # GET /sparks.json
  def index
    if params[:limit]
      @sparks = Spark.order("id DESC").limit(params[:limit])
    else
      @sparks = Spark.order("id DESC")
    end
    
    respond_with @sparks
  end
  
  # GET /sparks/1.json
  def show
    @spark = Spark.find(params[:id])
    
    respond_with @spark
  end
  
  # POST /sparks.json
  def create
    @spark = Spark.new(params[:spark])
        
    if @spark.save
      add_tags_to @spark
      
      @user.sparks << @spark
    elsif @spark.duplicate?
      # TODO: Handle the different spark_types provided by multiple users
      
      @spark = Spark.find_by_content_hash(@spark.content_hash)
      
      unless(@spark.users.include?(@user))
        @spark.users << @user
      end
    end
    
    respond_with @spark, :location => ["v1", @spark]
  end
  
  # DELETE /sparks/1.json
  def destroy
    @spark = Spark.find(params[:id])
    
    if @spark && @spark.users.include?(@user)
      @spark.users.delete(@user)
      
      respond_to do |format|
        format.json { render :json => @spark, :status => :ok }
      end
    end
  end
  
end
