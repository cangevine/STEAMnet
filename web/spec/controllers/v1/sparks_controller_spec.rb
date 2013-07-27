require 'spec_helper'

describe V1::SparksController do
  
  describe "GET 'index'" do
    
    before do
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
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == @sparks.length
      
      output.each_with_index do |spark, index|
        spark["content_hash"].should == @sparks[index].content_hash
      end
    end
    
    it "limits the sparks correctly" do
      get :index, :format => 'json', :limit => 10, :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Array)
      output.length.should == 10
      
      output.each_with_index do |spark, index|
        spark["content_hash"].should == @sparks[index].content_hash
      end
    end
    
    describe "lite response" do
      
      it "only returns ids and stuff" do
        get :index, :format => 'json', :lite => "true", :token => @auth_token
        output = JSON.parse(response.body)
        
        output.should be_a_kind_of(Array)
        
        output.each do |spark|
          spark["id"].should_not be_nil
          spark["users"].should be_nil
          spark["ideas"].should be_nil
        end
      end
      
    end
    
  end
  
  describe "GET 'show'" do
    
    before do
      @spark = FactoryGirl.create(:spark)
    end
    
    it "is successful" do
      get :show, :id => @spark, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "returns the correct spark" do
      get :show, :id => @spark, :format => 'json', :token => @auth_token
      output = JSON.parse(response.body)
      
      output.should be_a_kind_of(Hash)
      output["content_hash"].should == @spark.content_hash
      output["file"].should_not be_nil
    end
    
    describe "lite response" do
      
      it "only returns id and stuff" do
        get :show, :id => @spark, :format => 'json', :lite => "true", :token => @auth_token
        output = JSON.parse(response.body)
        
        output.should be_a_kind_of(Hash)
        
        output["id"].should_not be_nil
        output["users"].should be_nil
        output["ideas"].should be_nil
      end
      
    end
    
  end
  
  describe "POST 'create'" do
    
    before do
      @attr = {
        :spark_type   => "I",
        :content_type => "P",
        :content      => "Picture title",
        :file         => fixture_file_upload('spec/fixtures/images/test.jpg', 'image/jpeg')
      }
    end
    
    describe "for a new valid spark" do
      
      it "is successful" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        response.should be_success
      end
    
      it "should create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :token => @auth_token
        }.to change { Spark.count }.by(1)
        
        Spark.last.content.should == @attr[:content]
      end
      
      it "should add the user to the spark" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        Spark.last.users.should == [@test_user]
      end
      
      it "should add tags to the spark" do
        t1 = FactoryGirl.create(:tag)
        t2 = FactoryGirl.create(:tag)
        t3 = FactoryGirl.create(:tag)
        
        tags = [t1,t2,t3].map(&:tag_text).join(",")
        
        post :create, :spark => @attr, :format => 'json', :tags => tags, :token => @auth_token
        
        Spark.last.tags.should == [t1,t2,t3]
      end
      
      it "should create new tags" do
        t1 = "foo"
        t2 = "bar"
        t3 = "purple"
        
        tags = [t1,t2,t3].join(",")
        
        post :create, :spark => @attr, :format => 'json', :tags => tags, :token => @auth_token
        
        [t1,t2,t3].each do |t|
          Tag.find_by(tag_text: t).should_not be_nil
        end
      end
      
      it "should return the spark" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Hash)
        output["content_hash"].should == Spark.last.content_hash
        output["tags"].should == Spark.last.tags.map(&:tag_text)
        output["spark_is_new"].should == true
      end
      
    end
    
    describe "for a new invalid spark" do
      
      before do
        @attr[:content_type] = ""
      end
      
      it "isn't successful" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        response.should_not be_success
      end
    
      it "shouldn't create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :token => @auth_token
        }.not_to change { Spark.count }
      end
      
    end
    
    describe "for an existing spark" do
      
      before do
        @spark = Spark.create(@attr)
        @spark.users << @test_user
        
        @user2 = FactoryGirl.create(:user)
        @auth_token = @user2.devices.create(registration_id: "whatever").token
      end
      
      it "is successful" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        response.should be_success
      end
    
      it "shouldn't create the spark" do
        expect {
          post :create, :spark => @attr, :format => 'json', :token => @auth_token
        }.not_to change { Spark.count }
      end
      
      it "should add the user to the spark" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        @spark.users.should == [@test_user, @user2]
      end
      
      it "should return the spark" do
        post :create, :spark => @attr, :format => 'json', :token => @auth_token
        output = JSON.parse(response.body)

        output.should be_a_kind_of(Hash)
        output["content_hash"].should == @spark.content_hash
        output["spark_is_new"].should_not == true
      end
      
    end
    
  end
  
  describe "DELETE 'destroy'" do
    
    before do
      @spark = FactoryGirl.create(:spark)
      
      @spark.users << @test_user
    end
    
    it "is successful" do
      delete :destroy, :id => @spark, :format => 'json', :token => @auth_token
      response.should be_success
    end
    
    it "doesn't destroy the spark" do
      expect {
        delete :destroy, :id => @spark, :format => 'json', :token => @auth_token
      }.not_to change { Spark.count }
    end
    
    it "removes the user from the spark" do
      delete :destroy, :id => @spark, :format => 'json', :token => @auth_token
      @spark.users.include?(@test_user).should_not be_true
    end
    
    it "returns the spark" do
      delete :destroy, :id => @spark, :format => 'json', :token => @auth_token
      @spark.reload
      output = JSON.parse(response.body)

      output.should be_a_kind_of(Hash)
      output["content_hash"].should == @spark.content_hash
    end
    
  end
  
end
