require 'spec_helper'

describe V1::IdeasController do
  
  describe "GET 'index'" do
    
    before(:each) do
      @ideas = []
      
      20.times do
        @ideas << FactoryGirl.create(:idea)
      end
      
      @ideas.reverse!
    end
    
    it "is successful" do
      get :index, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct ideas" do
      get :index, :format => 'json'
      response.body.should == @ideas.to_json
    end
    
    it "limits the ideas correctly" do
      get :index, :format => 'json', :limit => 10
      response.body.should == @ideas.take(10).to_json
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @idea = FactoryGirl.create(:idea)
    end
    
    it "is successful" do
      get :show, :id => @idea, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct idea" do
      get :show, :id => @idea, :format => 'json'
      response.body.should == @idea.to_json
    end
    
  end
  
  describe "POST 'create'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
      
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @sparks = [@s1,@s2].map(&:id).join(",")
      
      @attr = {
        :description  => "I"
      }
    end
    
    describe "for a new valid idea" do
      
      it "is successful" do
        post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => @sparks
        response.should be_success
      end
    
      it "should create the idea" do
        expect {
          post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => @sparks
        }.to change { Idea.count }.by(1)
      end
      
      it "should add the user to the idea" do
        post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => @sparks
        @idea = Idea.last
        @idea.users.should == [@user]
      end
      
      it "should add the sparks to the idea" do
        post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => @sparks
        @idea = Idea.last
        @idea.sparks.should == [@s1, @s2]
      end
      
      it "should return the idea" do
        post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => @sparks
        @idea = Idea.last
        response.body.should == @idea.to_json
      end
      
    end
    
    describe "for an idea without sparks" do
      
      it "isn't successful" do
        post :create, :idea => @attr, :format => 'json', :username => @user.name
        response.should_not be_success
      end
    
      it "shouldn't create the idea" do
        expect {
          post :create, :idea => @attr, :format => 'json', :username => @user.name
        }.not_to change { Idea.count }
      end
      
    end
    
    describe "for an idea with invalid sparks" do
      
      it "isn't successful" do
        post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => "100,150"
        response.should_not be_success
      end
    
      it "shouldn't create the idea" do
        expect {
          post :create, :idea => @attr, :format => 'json', :username => @user.name, :sparks => "100,150"
        }.not_to change { Idea.count }
      end
      
    end
  
  end
  
  describe "DELETE 'destroy'" do
    
    before(:each) do
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @user = FactoryGirl.create(:user)
      
      @idea = FactoryGirl.create(:idea)
      
      @idea.sparks << @s1
      @idea.sparks << @s2
      
      @idea.users << @user
    end
    
    it "is successful" do
      delete :destroy, :id => @idea, :format => 'json', :username => @user.name
      response.should be_success
    end
    
    it "doesn't destroy the idea" do
      expect {
        delete :destroy, :id => @idea, :format => 'json', :username => @user.name
      }.not_to change { Idea.count }
    end
    
    it "removes the user from the idea" do
      delete :destroy, :id => @idea, :format => 'json', :username => @user.name
      @idea.users.include?(@user).should_not be_true
    end
    
    it "returns the idea" do
      delete :destroy, :id => @idea, :format => 'json', :username => @user.name
      @idea = Idea.find_by_id(@idea.id) # used to force @spark to reload its attributes
      response.body.should == @idea.to_json
    end
    
  end
  
end
