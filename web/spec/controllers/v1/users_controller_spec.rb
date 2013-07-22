require 'spec_helper'

describe V1::UsersController do
  
  describe "GET 'index'" do
    
    before do
      @users = [@test_user]
      
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
      output.length.should == User.all.length
      
      output.each_with_index do |user, index|
        user["name"].should == @users[index].name
      end
    end
    
  end
  
  describe "GET 'show'" do
    
    before do
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
  
  describe "PUT 'update'" do
    
    before do
      @attr = {
        :name     => "max",
        :email    => "max@luzuriaga.com"
      }
    end
    
    it "is successful" do
      patch :update, :id => @test_user, :user => @attr, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "updates the user" do
      patch :update, :id => @test_user, :user => @attr, :format => 'json', :token => @auth_token
      @test_user.reload
      @test_user.name.should == @attr[:name]
      @test_user.email.should == @attr[:email]
    end
    
    it "returns the user" do
      patch :update, :id => @test_user, :user => @attr, :format => 'json', :token => @auth_token
      
      @test_user.reload
      
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["name"].should == @test_user.name
    end
    
  end
  
  describe "DELETE 'destroy'" do
    
    it "is successful" do
      delete :destroy, :id => @test_user, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "destroys the user" do
      expect {
        delete :destroy, :id => @test_user, :format => 'json', :token => @auth_token
      }.to change { User.count }.by(-1)
    end
    
  end
  
end
