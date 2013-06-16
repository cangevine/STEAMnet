require 'spec_helper'

describe V1::JawnsController do
  
  describe "GET 'index'" do
    
    before(:each) do
      @jawns = []
      @ideas = []
      @sparks = []
      
      20.times do
        if [true, false].sample
          idea = FactoryGirl.create(:idea)
          @jawns << idea
          @ideas << idea
        else
          spark = FactoryGirl.create(:spark)
          @jawns << spark
          @sparks << spark
        end
      end
      
      @jawns.reverse!
    end
    
    it "is successful" do
      get :index, :format => 'json'
      response.should be_success
    end
    
    it "returns the correct jawns" do
      get :index, :format => 'json'
      response.body.should == @jawns.to_json
    end
    
    it "limits the jawns correctly" do
      get :index, :format => 'json', :limit => 10
      response.body.should == @jawns.take(10).to_json
    end
    
    describe "idea filtering" do
      
      it "filters ideas correctly" do
        get :index, :format => 'json', :filter => "ideas"
        response.body.should == @ideas.to_json
      end
      
      it "filters ideas with a limit correctly" do
        get :index, :format => 'json', :filter => "ideas", :limit => 3
        response.body.should == @ideas.take(10).to_json
      end
      
    end
    
    describe "spark filtering" do
      
      it "filters sparks correctly" do
        get :index, :format => 'json', :filter => "sparks"
        response.body.should == @sparks.to_json
      end
      
      it "filters sparks with a limit correctly" do
        get :index, :format => 'json', :filter => "sparks", :limit => 3
        response.body.should == @sparks.take(10).to_json
      end
      
    end
    
  end
  
end
