class V1::CommentsController < ApplicationController
  
  respond_to :json
  before_filter :find_jawn
  before_filter :find_comment, :only => [:show, :destroy]
  before_filter :authenticate, :only => [:create, :update, :destroy]
  
  # GET /comments.json
  def index
    respond_with @jawn.comments
  end
  
  # GET /comments/1.json
  def show
    respond_with @comment
  end
  
  # POST /comments.json
  def create
    @comment = Comment.new(params[:comment])
    @comment.commentable = @jawn
    @comment.user = @user
    
    @comment.save
    
    respond_with @comment, :location => ["v1", @jawn, @comment]
  end
  
  # DELETE /comments/1.json
  def destroy
    if @comment.user == @user
      @comment.destroy
      head :no_content
    else
      head :unauthorized
    end
  end
  
  private
  
    def find_jawn
      if params[:spark_id]
        @jawn = Spark.find(params[:spark_id].to_i)
      elsif params[:idea_id]
        @jawn = Idea.find(params[:idea_id].to_i)
      end
      
      unless @jawn
        hash = { :error => "No jawn with specified id found." }
        respond_to do |format|
          format.json { render :json => hash, :status => :unauthorized }
        end
      end
    end
    
    def find_comment
      @comment = @jawn.comments.find(params[:id])
    end
  
end
