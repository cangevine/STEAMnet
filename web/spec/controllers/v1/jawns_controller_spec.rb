require 'spec_helper'

describe V1::JawnsController do
  
  describe "GET 'index'" do
    
    before do
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
      @ideas.reverse!
      @sparks.reverse!
    end
    
    it "is successful" do
      get :index, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct jawns" do
      get :index, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == @jawns.length
      
      output.each_with_index do |jawn, index|
        jawn["jawn_type"].should == @jawns[index].class.to_s.downcase
      end
    end
    
    it "limits the jawns correctly" do
      get :index, :format => 'json', :limit => 10, :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == 10
      
      output.each_with_index do |jawn, index|
        jawn["jawn_type"].should == @jawns[index].class.to_s.downcase
      end
    end
    
    describe "lite response" do
      
      it "only returns ids and jawn_types" do
        get :index, :format => 'json', :lite => "true", :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Array)
        
        output.each do |jawn|
          jawn["id"].should_not be_nil
          jawn["jawn_type"].should_not be_nil
          jawn["users"].should be_nil
          jawn["user"].should be_nil
          jawn["sparks"].should be_nil
          jawn["ideas"].should be_nil
        end
      end
      
    end
    
    describe "idea filtering" do
      
      it "filters ideas correctly" do
        get :index, :format => 'json', :filter => "ideas", :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Array)
        output.length.should == @ideas.length

        output.each_with_index do |jawn, index|
          jawn["jawn_type"].should == "idea"
          jawn["description"].should == @ideas[index].description
        end
      end
      
      it "filters ideas with a limit correctly" do
        get :index, :format => 'json', :filter => "ideas", :limit => 3, :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Array)
        output.length.should == 3

        output.each_with_index do |jawn, index|
          jawn["jawn_type"].should == "idea"
          jawn["description"].should == @ideas[index].description
        end
      end
      
    end
    
    describe "spark filtering" do
      
      it "filters sparks correctly" do
        get :index, :format => 'json', :filter => "sparks", :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Array)
        output.length.should == @sparks.length

        output.each_with_index do |jawn, index|
          jawn["jawn_type"].should == "spark"
          jawn["content_hash"].should == @sparks[index].content_hash
        end
      end
      
      it "filters sparks with a limit correctly" do
        get :index, :format => 'json', :filter => "sparks", :limit => 3, :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Array)
        output.length.should == 3

        output.each_with_index do |jawn, index|
          jawn["jawn_type"].should == "spark"
          jawn["content_hash"].should == @sparks[index].content_hash
        end
      end
      
    end
    
  end
  
end
