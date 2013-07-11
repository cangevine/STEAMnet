class V1::UsersController < ApplicationController
  
  respond_to :json
  
  # GET /users.json
  def index
    @users = User.all
  end
  
  # GET /users/1.json
  def show
    @user = User.find_by(name: params[:id])
  end
  
  # POST /users.json
  def create
    @user = User.new(user_params)
    
    if @user.save
      render "show"
    else
      head :unprocessable_entity
    end
  end
  
  # PUT /users/1.json
  def update
    @user = User.find_by(name: params[:id])
    
    if @user.update_attributes(user_params)
      render "show", :status => :ok
    end
  end
  
  # DELETE /users/1.json
  def destroy
    @user = User.find_by(name: params[:id])
    @user.destroy
    
    head :no_content
  end
  
  private
  
    def user_params
      params.require(:user).permit(:email, :name, :password, :password_confirmation)
    end
  
end
