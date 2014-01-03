class V1::SparksController < ApplicationController
  
  respond_to :json
  before_action :authenticate, :only => [:create, :destroy]
  
  # GET /sparks.json
  def index
    if params[:limit]
      @sparks = Spark.order("id DESC").limit(params[:limit])
    else
      @sparks = Spark.order("id DESC")
    end
  end
  
  # GET /sparks/1.json
  def show
    @spark = Spark.find(params[:id])
  end
  
  # POST /sparks.json
  def create
    @spark = Spark.new(spark_params)
    
    if @spark.save
      add_tags_to @spark
      
      @user.sparks << @spark
      
      @spark_is_new = true
      
      render "show"
    elsif @spark.duplicate?
      # TODO: Handle the different spark_types provided by multiple users
      
      @spark = Spark.find_by(content_hash: @spark.content_hash)
      
      unless(@spark.users.include?(@user))
        @spark.users << @user
      end
      
      render "show"
    else
      head :unprocessable_entity
    end
  end
  
  # DELETE /sparks/1.json
  def destroy
    @spark = Spark.find(params[:id])
    
    if @spark && @spark.users.include?(@user)
      @spark.users.delete(@user)
      
      render "show", :status => :ok
    end
  end
  
  private
  
    def spark_params
      params.require(:spark).permit(:content, :content_hash, :content_type, :spark_type, :file)
    end
  
end
