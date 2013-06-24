require 'spec_helper'

describe V1::TagsController do
  
  describe "GET 'index'" do
    
    before(:each) do
      @tags = []
      
      5.times do
        @tags << FactoryGirl.create(:tag)
      end
    end
    
    it "is successful" do
      get :index, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct tags" do
      get :index, :format => 'json', :token => @auth_token
      response.body.should == @tags.to_json
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @tag = FactoryGirl.create(:tag)
    end
    
    it "is successful" do
      get :show, :id => @tag, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct tag" do
      get :show, :id => @tag, :format => 'json', :token => @auth_token
      response.body.should == @tag.to_json
    end
    
  end
  
end
