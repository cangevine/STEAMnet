require 'spec_helper'

describe V1::JawnsController do
  
  describe "GET 'index'" do
    
    before(:each) do
      @jawns = []
      
      20.times do
        if [true, false].sample
          @jawns << FactoryGirl.create(:idea)
        else
          @jawns << FactoryGirl.create(:spark)
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
    
  end
  
end
