class V1::UsersController < ApplicationController
  
  respond_to :json
  
  # GET /users.json
  def index
    @users = User.all

    respond_with @users
  end

  # GET /users/1
  # GET /users/1.json
  def show
    @user = User.find_by_name(params[:id])

    respond_with @user
  end

  # POST /users
  # POST /users.json
  def create
    @user = User.new(params[:user])
    @user.save
    
    respond_with @user, :location => ["v1", @user]
  end

  # PUT /users/1
  # PUT /users/1.json
  def update
    @user = User.find_by_name(params[:id])
    @user.update_attributes(params[:user])
    
    respond_with @user, :stautus => :ok
  end

  # DELETE /users/1
  # DELETE /users/1.json
  def destroy
    @user = User.find_by_name(params[:id])
    @user.destroy
    
    head :no_content
  end
end
