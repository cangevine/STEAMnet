require 'spec_helper'

describe V1::UsersController do
  
  describe "GET 'index'" do
    
    before(:each) do
      @users = []
      
      20.times do
        @users << FactoryGirl.create(:user)
      end
    end
    
    it "is successful" do
      get :index, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct users" do
      get :index, :format => 'json'
      response.body.should == @users.to_json
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
    end
    
    it "is successful" do
      get :show, :id => @user, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct user" do
      get :show, :id => @user, :format => 'json'
      response.body.should == @user.to_json
    end
    
  end
  
  describe "POST 'create'" do
    
    before(:each) do
      @attr = {
        :name     => "max",
        :email    => "max@luzuriaga.com",
        :password => "password"
      }
    end
    
    describe "for a new valid user" do
      
      it "is successful" do
        post :create, :user => @attr, :format => 'json'
        response.should be_success
      end
    
      it "should create the user" do
        expect {
          post :create, :user => @attr, :format => 'json'
        }.to change { User.count }.by(1)
      end
      
      it "should return the user" do
        post :create, :user => @attr, :format => 'json'
        response.body.should == User.find_by_name(@attr[:name]).to_json
      end
      
    end
    
    describe "for a new invalid user" do
      
      before(:each) do
        @attr[:name] = ""
      end
      
      it "isn't successful" do
        post :create, :user => @attr, :format => 'json'
        response.should_not be_success
      end
    
      it "shouldn't create the user" do
        expect {
          post :create, :user => @attr, :format => 'json'
        }.not_to change { User.count }
      end
      
    end
    
  end
  
  describe "PUT 'update'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
      
      @attr = {
        :name     => "max",
        :email    => "max@luzuriaga.com"
      }
    end
    
    it "is successful" do
      put :update, :id => @user, :user => @attr, :format => 'json'
      response.should be_success
    end
    
    it "updates the user" do
      put :update, :id => @user, :user => @attr, :format => 'json'
      @user = User.find_by_id(@user.id)
      @user.name.should == @attr[:name]
      @user.email.should == @attr[:email]
    end
    
  end
  
  describe "DELETE 'destroy'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
    end
    
    it "is successful" do
      delete :destroy, :id => @user, :format => 'json'
      response.should be_success
    end
    
    it "destroys the user" do
      expect {
        delete :destroy, :id => @user, :format => 'json'
      }.to change { User.count }.by(-1)
    end
    
  end
  
end
