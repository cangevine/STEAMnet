class V1::UsersController < ApplicationController
  
  respond_to :json
  
  # GET /users.json
  def index
    @users = User.all

    respond_with @users
  end
  
  # GET /users/1.json
  def show
    @user = User.find_by(name: params[:id])

    respond_with @user
  end
  
  # POST /users.json
  def create
    @user = User.new(user_params)
    @user.save
    
    respond_with @user, :location => ["v1", @user]
  end
  
  # PUT /users/1.json
  def update
    @user = User.find_by(name: params[:id])
    @user.update_attributes(user_params)
    
    respond_with @user, :stautus => :ok
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
