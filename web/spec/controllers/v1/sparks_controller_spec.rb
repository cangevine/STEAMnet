require 'spec_helper'

describe V1::SparksController do
  
  describe "GET 'index'" do
    
    before(:each) do
      @sparks = []
      
      20.times do
        @sparks << FactoryGirl.create(:spark)
      end
      
      @sparks.reverse!
    end
    
    it "is successful" do
      get :index, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct sparks" do
      get :index, :format => 'json', :token => @auth_token
      response.body.should == @sparks.to_json
    end
    
    it "limits the sparks correctly" do
      get :index, :format => 'json', :limit => 10, :token => @auth_token
      response.body.should == @sparks.take(10).to_json
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @spark = FactoryGirl.create(:spark)
    end
    
    it "is successful" do
      get :show, :id => @spark, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct spark" do
      get :show, :id => @spark, :format => 'json', :token => @auth_token
      response.body.should == @spark.to_json
    end
    
  end
  
  describe "POST 'create'" do
    
    before(:each) do
      @user = FactoryGirl.create(:user)
      
      @attr = {
        :spark_type   => "I",
        :content_type => "L",
        :content      => "http://google.com/"
      }
    end
    
    describe "for a new valid spark" do
      
      it "is successful" do
        post :create, :spark => @attr, :format => 'json', :username => @user.name, :token => @auth_token
        response.should be_success
      end
    
      it "should create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :username => @user.name, :token => @auth_token
        }.to change { Spark.count }.by(1)
      end
      
      it "should add the user to the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user.name, :token => @auth_token
        @spark = Spark.last
        @spark.users.should == [@user]
      end
      
      it "should add tags to the spark" do
        t1 = FactoryGirl.create(:tag)
        t2 = FactoryGirl.create(:tag)
        t3 = FactoryGirl.create(:tag)
        
        tags = [t1,t2,t3].map(&:tag_text).join(",")
        
        post :create, :spark => @attr, :format => 'json', :username => @user.name, :tags => tags, :token => @auth_token
        
        @spark = Spark.last
        @spark.tags.should == [t1,t2,t3]
      end
      
      it "should create new tags" do
        t1 = "foo"
        t2 = "bar"
        t3 = "purple"
        
        tags = [t1,t2,t3].join(",")
        
        post :create, :spark => @attr, :format => 'json', :username => @user.name, :tags => tags, :token => @auth_token
        
        [t1,t2,t3].each do |t|
          Tag.find_by(tag_text: t).should_not be_nil
        end
      end
      
      it "should return the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user.name, :token => @auth_token
        @spark = Spark.last
        response.body.should == @spark.to_json
      end
      
    end
    
    describe "for a new invalid spark" do
      
      before(:each) do
        @attr[:content_type] = ""
      end
      
      it "isn't successful" do
        post :create, :spark => @attr, :format => 'json', :username => @user.name, :token => @auth_token
        response.should_not be_success
      end
    
      it "shouldn't create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :username => @user.name, :token => @auth_token
        }.not_to change { Spark.count }
      end
      
    end
    
    describe "for an existing spark" do
      
      before(:each) do
        @spark = Spark.create(@attr)
        @spark.users << @user
        
        @user2 = FactoryGirl.create(:user)
      end
      
      it "is successful" do
        post :create, :spark => @attr, :format => 'json', :username => @user2.name, :token => @auth_token
        response.should be_success
      end
    
      it "shouldn't create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :username => @user2.name, :token => @auth_token
        }.not_to change { Spark.count }
      end
      
      it "should add the user to the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user2.name, :token => @auth_token
        @spark.users.should == [@user, @user2]
      end
      
      it "should return the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user2.name, :token => @auth_token
        response.body.should == @spark.to_json
      end
      
    end
    
  end
  
  describe "DELETE 'destroy'" do
    
    before(:each) do
      @spark = FactoryGirl.create(:spark)
      @user = FactoryGirl.create(:user)
      
      @spark.users << @user
    end
    
    it "is successful" do
      delete :destroy, :id => @spark, :format => 'json', :username => @user.name, :token => @auth_token
      response.should be_success
    end
    
    it "doesn't destroy the spark" do
      expect {
        delete :destroy, :id => @spark, :format => 'json', :username => @user.name, :token => @auth_token
      }.not_to change { Spark.count }
    end
    
    it "removes the user from the spark" do
      delete :destroy, :id => @spark, :format => 'json', :username => @user.name, :token => @auth_token
      @spark.users.include?(@user).should_not be_true
    end
    
    it "returns the spark" do
      delete :destroy, :id => @spark, :format => 'json', :username => @user.name, :token => @auth_token
      @spark.reload
      response.body.should == @spark.to_json
    end
    
  end
  
end
