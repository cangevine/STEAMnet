require 'spec_helper'

describe V1::IdeasController do
  
  describe "GET 'index'" do
    
    before do
      @ideas = []
      
      20.times do
        @ideas << FactoryGirl.create(:idea)
      end
      
      @ideas.reverse!
    end
    
    it "is successful" do
      get :index, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct ideas" do
      get :index, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == @ideas.length
      
      output.each_with_index do |idea, index|
        idea["description"].should == @ideas[index].description
      end
    end
    
    it "limits the ideas correctly" do
      get :index, :format => 'json', :limit => 10, :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == 10
      
      output.each_with_index do |idea, index|
        idea["description"].should == @ideas[index].description
      end
    end
    
    describe "lite response" do
      
      it "only returns ids and stuff" do
        get :index, :format => 'json', :lite => "true", :token => @auth_token
        output = JSON.parse(response.body)
        
        output.should be_a_kind_of(Array)
        
        output.each do |idea|
          idea["id"].should_not be_nil
          idea["user"].should be_nil
          idea["sparks"].should be_nil
        end
      end
      
    end
    
  end
  
  describe "GET 'show'" do
    
    before do
      @idea = FactoryGirl.create(:idea)
    end
    
    it "is successful" do
      get :show, :id => @idea, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct idea" do
      get :show, :id => @idea, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["description"].should == @idea.description
    end
    
    describe "lite response" do
      
      it "only returns id and stuff" do
        get :show, :id => @idea, :format => 'json', :lite => "true", :token => @auth_token
        output = JSON.parse(response.body)
        
        output.should be_a_kind_of(Hash)
        
        output["id"].should_not be_nil
        output["user"].should be_nil
        output["sparks"].should be_nil
      end
      
    end
    
  end
  
  describe "POST 'create'" do
    
    before do
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @sparks = [@s1,@s2].map(&:id).join(",")
      
      @attr = {
        :description  => "I"
      }
    end
    
    describe "for a new valid idea" do
      
      it "is successful" do
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :token => @auth_token
        response.should be_success
      end
    
      it "should create the idea" do
        expect {
          post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :token => @auth_token
        }.to change { Idea.count }.by(1)
      end
      
      it "should add the user to the idea" do
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :token => @auth_token
        @idea = Idea.last
        @idea.user.should == @test_user
      end
      
      it "should add the sparks to the idea" do
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :token => @auth_token
        @idea = Idea.last
        @idea.sparks.should == [@s1, @s2]
      end
      
      it "should ignore invalid sparks" do
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks + ",100", :token => @auth_token
        @idea = Idea.last
        @idea.sparks.should == [@s1, @s2]
      end
      
      it "should add tags to the spark" do
        t1 = FactoryGirl.create(:tag)
        t2 = FactoryGirl.create(:tag)
        t3 = FactoryGirl.create(:tag)
        
        tags = [t1,t2,t3].map(&:tag_text).join(",")
        
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :tags => tags, :token => @auth_token
        
        @idea = Idea.last
        @idea.tags.should == [t1,t2,t3]
      end
      
      it "should create new tags" do
        t1 = "foo"
        t2 = "bar"
        t3 = "purple"
        
        tags = [t1,t2,t3].join(",")
        
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :tags => tags, :token => @auth_token
        
        [t1,t2,t3].each do |t|
          Tag.find_by(tag_text: t).should_not be_nil
        end
      end
      
      it "should return the idea" do
        post :create, :idea => @attr, :format => 'json', :sparks => @sparks, :token => @auth_token
        @idea = Idea.last
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Hash)
        output["description"].should == @idea.description
      end
      
    end
    
    describe "for an idea without sparks" do
      
      it "isn't successful" do
        post :create, :idea => @attr, :format => 'json', :token => @auth_token
        response.should_not be_success
      end
    
      it "shouldn't create the idea" do
        expect {
          post :create, :idea => @attr, :format => 'json', :token => @auth_token
        }.not_to change { Idea.count }
      end
      
    end
    
    describe "for an idea with invalid sparks" do
      
      it "isn't successful" do
        post :create, :idea => @attr, :format => 'json', :sparks => "100,150", :token => @auth_token
        response.should_not be_success
      end
    
      it "shouldn't create the idea" do
        expect {
          post :create, :idea => @attr, :format => 'json', :sparks => "100,150", :token => @auth_token
        }.not_to change { Idea.count }
      end
      
    end
  
  end
  
  describe "DELETE 'destroy'" do
    
    before do
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @idea = FactoryGirl.create(:idea)
      
      @idea.sparks << @s1
      @idea.sparks << @s2
      
      @idea.user = @test_user
      @idea.save
    end
    
    it "is successful" do
      delete :destroy, :id => @idea, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "doesn't destroy the idea" do
      expect {
        delete :destroy, :id => @idea, :format => 'json', :token => @auth_token
      }.not_to change { Idea.count }
    end
    
    it "removes the user from the idea" do
      delete :destroy, :id => @idea, :format => 'json', :token => @auth_token
      @idea.reload
      @idea.user.should be_nil
    end
    
    it "returns the idea" do
      delete :destroy, :id => @idea, :format => 'json', :token => @auth_token
      @idea.reload
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["description"].should == @idea.description
    end
    
  end
  
end
