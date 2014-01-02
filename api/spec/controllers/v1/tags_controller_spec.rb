require 'spec_helper'

describe V1::TagsController do
  
  describe "GET 'index'" do
    
    before do
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
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == @tags.length
      
      output.each_with_index do |tag, index|
        tag["tag_text"].should == @tags[index].tag_text
      end
    end
    
  end
  
  describe "GET 'show'" do
    
    before do
      @tag = FactoryGirl.create(:tag)
    end
    
    it "is successful" do
      get :show, :id => @tag.tag_text, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct tag" do
      get :show, :id => @tag.tag_text, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["tag_text"].should == @tag.tag_text
      output["jawns"].should be_a_kind_of(Array)
    end
    
  end
  
end
