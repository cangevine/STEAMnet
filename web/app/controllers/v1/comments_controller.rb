class V1::CommentsController < ApplicationController
  
  respond_to :json
  before_action :find_jawn
  before_action :find_comment, :only => [:show, :destroy]
  before_action :authenticate, :only => [:create, :update, :destroy]
  
  # GET /comments.json
  def index
    @comments = @jawn.comments
  end
  
  # GET /comments/1.json
  def show
    # Easiest function ever
  end
  
  # POST /comments.json
  def create
    @comment = Comment.new(comment_params)
    @comment.commentable = @jawn
    @comment.user = @user
    
    @comment.save
    
    render "show"
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
    
    def comment_params
      params.require(:comment).permit(:comment_text)
    end
    
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
