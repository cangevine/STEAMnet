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
      get :index, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct sparks" do
      get :index, :format => 'json'
      response.body.should == @sparks.to_json
    end
    
    it "limits the sparks correctly" do
      get :index, :format => 'json', :limit => 10
      response.body.should == @sparks.take(10).to_json
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @spark = FactoryGirl.create(:spark)
    end
    
    it "is successful" do
      get :show, :id => @spark, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct spark" do
      get :show, :id => @spark, :format => 'json'
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
        post :create, :spark => @attr, :format => 'json', :username => @user.name
        response.should be_success
      end
    
      it "should create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :username => @user.name
        }.to change { Spark.count }.by(1)
      end
      
      it "should add the user to the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user.name
        spark = Spark.last
        spark.users.should == [@user]
      end
      
      it "should return the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user.name
        spark = Spark.last
        response.body.should == spark.to_json
      end
      
    end
    
    describe "for an existing spark spark" do
      
      before(:each) do
        @spark = Spark.create(@attr)
        @spark.users << @user
        
        @user2 = FactoryGirl.create(:user)
      end
      
      it "is successful" do
        post :create, :spark => @attr, :format => 'json', :username => @user2.name
        response.should be_success
      end
    
      it "shouldn't create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :username => @user2.name
        }.not_to change { Spark.count }
      end
      
      it "should add the user to the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user2.name
        @spark.users.should == [@user, @user2]
      end
      
      it "should return the spark" do
        post :create, :spark => @attr, :format => 'json', :username => @user2.name
        response.body.should == @spark.to_json
      end
      
    end
    
  end
  
end
