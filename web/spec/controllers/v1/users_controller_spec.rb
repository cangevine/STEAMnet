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
      get :index, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct users" do
      get :index, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == @users.length
      
      output.each_with_index do |user, index|
        user["name"].should == @users[index].name
      end
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
    end
    
    it "is successful" do
      get :show, :id => @user, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct user" do
      get :show, :id => @user, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["name"].should == @user.name
    end
    
  end
  
  describe "POST 'create'" do
    
    before(:each) do
      @attr = {
        :name                   => "max",
        :email                  => "max@luzuriaga.com",
        :password               => "password",
        :password_confirmation  => "password"
      }
    end
    
    describe "for a new valid user" do
      
      it "is successful" do
        post :create, :user => @attr, :format => 'json', :token => @auth_token
        response.should be_success
      end
    
      it "should create the user" do
        expect {
          post :create, :user => @attr, :format => 'json', :token => @auth_token
        }.to change { User.count }.by(1)
      end
      
      it "should return the user" do
        post :create, :user => @attr, :format => 'json', :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Hash)
        output["name"].should == @attr[:name]
      end
      
    end
    
    describe "for a new invalid user" do
      
      before(:each) do
        @attr[:name] = ""
      end
      
      it "isn't successful" do
        post :create, :user => @attr, :format => 'json', :token => @auth_token
        response.should_not be_success
      end
    
      it "shouldn't create the user" do
        expect {
          post :create, :user => @attr, :format => 'json', :token => @auth_token
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
      patch :update, :id => @user, :user => @attr, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "updates the user" do
      patch :update, :id => @user, :user => @attr, :format => 'json', :token => @auth_token
      @user.reload
      @user.name.should == @attr[:name]
      @user.email.should == @attr[:email]
    end
    
    it "returns the user" do
      patch :update, :id => @user, :user => @attr, :format => 'json', :token => @auth_token
      
      @user.reload
      
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["name"].should == @user.name
    end
    
  end
  
  describe "DELETE 'destroy'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
    end
    
    it "is successful" do
      delete :destroy, :id => @user, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "destroys the user" do
      expect {
        delete :destroy, :id => @user, :format => 'json', :token => @auth_token
      }.to change { User.count }.by(-1)
    end
    
  end
  
end
