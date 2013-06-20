require 'spec_helper'

describe V1::CommentsController do
  
  before(:each) do
    @user = FactoryGirl.create(:user)
    
    @spark = FactoryGirl.create(:spark)
    @idea = FactoryGirl.create(:idea)
  end
  
  describe "GET 'index'" do
    
    before(:each) do
      @spark_comments = []
      @idea_comments = []
      
      20.times do
        comment = FactoryGirl.create(:comment)
        comment.user = @user
        
        if [true, false].sample
          comment.commentable = @spark
          @spark_comments << comment
        else
          comment.commentable = @idea
          @idea_comments << comment
        end
        
        comment.save
      end
    end
    
    describe "on Sparks" do
      
      it "is successful" do
        get :index, :spark_id => @spark, :format => 'json', :token => @auth_token
        response.should be_success
      end

      it "returns the correct comments" do
        get :index, :spark_id => @spark, :format => 'json', :token => @auth_token
        response.body.should == @spark_comments.to_json
      end
      
    end
    
    describe "on Ideas" do
      
      it "is successful" do
        get :index, :idea_id => @idea, :format => 'json', :token => @auth_token
        response.should be_success
      end

      it "returns the correct comments" do
        get :index, :idea_id => @idea, :format => 'json', :token => @auth_token
        response.body.should == @idea_comments.to_json
      end
      
    end
    
  end
  
  describe "GET 'show'" do
    
    before(:each) do
      @spark_comment = FactoryGirl.create(:comment)
      @spark_comment.user = @user
      @spark_comment.commentable = @spark
      
      @spark_comment.save
      
      @idea_comment = FactoryGirl.create(:comment)
      @idea_comment.user = @user
      @idea_comment.commentable = @idea
      
      @idea_comment.save
    end
    
    describe "on a Spark" do
      
      describe "with a valid comment id" do
        
        it "is successful" do
          get :show, :spark_id => @spark, :id => @spark_comment, :format => 'json', :token => @auth_token
          response.should be_success
        end

        it "returns the correct comment" do
          get :show, :spark_id => @spark, :id => @spark_comment, :format => 'json', :token => @auth_token
          response.body.should == @spark_comment.to_json
        end
        
      end
      
      describe "with an invalid comment id" do
        
        it "isn't successful" do
          get :show, :spark_id => @spark, :id => @idea_comment, :format => 'json', :token => @auth_token
          response.should_not be_success
        end
        
      end
      
    end
    
    describe "on an Idea" do
      
      describe "with a valid comment id" do
        
        it "is successful" do
          get :show, :idea_id => @idea, :id => @idea_comment, :format => 'json', :token => @auth_token
          response.should be_success
        end

        it "returns the correct comment" do
          get :show, :idea_id => @idea, :id => @idea_comment, :format => 'json', :token => @auth_token
          response.body.should == @idea_comment.to_json
        end
        
      end
      
      describe "with an invalid comment id" do
        
        it "isn't successful" do
          get :show, :idea_id => @idea, :id => @spark_comment, :format => 'json', :token => @auth_token
          response.should_not be_success
        end
        
      end
      
    end
    
  end
  
  describe "POST 'create'" do
    
    before(:each) do
      @attr = {
        :comment_text => "This is a comment!"
      }
    end
    
    describe "on a Spark" do
      
      it "is successful" do
        post :create, :spark_id => @spark, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        response.should be_success
      end
    
      it "should create the comment" do
        expect {
          post :create, :spark_id => @spark, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        }.to change { Comment.count }.by(1)
      end
      
      it "should return the comment" do
        post :create, :spark_id => @spark, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        response.body.should == Comment.last.to_json
      end
      
      it "should associate the user and the comment" do
        post :create, :spark_id => @spark, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        comment = Comment.last
        comment.user.should == @user
      end
      
      it "should associate the spark and the comment" do
        post :create, :spark_id => @spark, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        comment = Comment.last
        comment.commentable.should == @spark
      end
      
    end
    
    describe "on an Idea" do
      
      it "is successful" do
        post :create, :idea_id => @idea, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        response.should be_success
      end
    
      it "should create the comment" do
        expect {
          post :create, :idea_id => @idea, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        }.to change { Comment.count }.by(1)
      end
      
      it "should return the comment" do
        post :create, :idea_id => @idea, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        response.body.should == Comment.last.to_json
      end
      
      it "should associate the user and the comment" do
        post :create, :idea_id => @idea, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        comment = Comment.last
        comment.user.should == @user
      end
      
      it "should associate the idea and the comment" do
        post :create, :idea_id => @idea, :comment => @attr, :username => @user.name, :format => 'json', :token => @auth_token
        comment = Comment.last
        comment.commentable.should == @idea
      end
      
    end
    
  end
  
  describe "DELETE 'destroy'" do
    
    before(:each) do
      @comment = FactoryGirl.create(:comment)
      @comment.user = @user
      @comment.save
    end
    
    describe "on a Spark" do
      
      before(:each) do
        @comment.commentable = @spark
      end
      
      describe "with the correct user" do
        
        it "is successful" do
          delete :destroy, :spark_id => @spark, :id => @comment, :username => @user.name, :format => 'json', :token => @auth_token
          response.should be_success
        end

        it "destroys the comment" do
          expect {
            delete :destroy, :spark_id => @spark, :id => @comment, :username => @user.name, :format => 'json', :token => @auth_token
          }.to change { Comment.count }.by(-1)
        end
        
      end
      
      describe "with the wrong user" do
        
        before(:each) do
          @wrong_user = FactoryGirl.create(:user)
        end
        
        it "isn't successful" do
          delete :destroy, :spark_id => @spark, :id => @comment, :username => @wrong_user.name, :format => 'json', :token => @auth_token
          response.should be_success
        end

        it "doesn't destroy the comment" do
          expect {
            delete :destroy, :spark_id => @spark, :id => @comment, :username => @wrong_user.name, :format => 'json', :token => @auth_token
          }.not_to change { Comment.count }
        end
        
      end
      
    end
    
    describe "on an Idea" do
      
      before(:each) do
        @comment.commentable = @idea
      end
      
      describe "with the correct user" do
        
        it "is successful" do
          delete :destroy, :idea_id => @idea, :id => @comment, :username => @user.name, :format => 'json', :token => @auth_token
          response.should be_success
        end

        it "destroys the comment" do
          expect {
            delete :destroy, :idea_id => @idea, :id => @comment, :username => @user.name, :format => 'json', :token => @auth_token
          }.to change { Comment.count }.by(-1)
        end
        
      end
      
      describe "with the wrong user" do
        
        before(:each) do
          @wrong_user = FactoryGirl.create(:user)
        end
        
        it "isn't successful" do
          delete :destroy, :idea_id => @idea, :id => @comment, :username => @wrong_user.name, :format => 'json', :token => @auth_token
          response.should be_success
        end

        it "doesn't destroy the comment" do
          expect {
            delete :destroy, :idea_id => @idea, :id => @comment, :username => @wrong_user.name, :format => 'json', :token => @auth_token
          }.not_to change { Comment.count }
        end
        
      end
      
    end
    
  end
  
end
