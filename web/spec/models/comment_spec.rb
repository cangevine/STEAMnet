# == Schema Information
#
# Table name: comments
#
#  id               :integer          not null, primary key
#  comment_text     :text
#  user_id          :integer
#  created_at       :datetime         not null
#  updated_at       :datetime         not null
#  commentable_id   :integer
#  commentable_type :string(255)
#

require 'spec_helper'

describe Comment do
  
  before do
    @attr = {
      :comment_text => "This is a comment"
    }
  end
  
  it "creates a new instance given valid attributes" do
    Comment.create!(@attr)
  end
  
  describe "validation" do
    
    it "requires comment text" do
      comment = Comment.new(@attr)
      comment.comment_text = ""
      comment.should_not be_valid
    end
    
  end
  
  describe "user association" do
    
    before do
      @comment = Comment.create(@attr)
      
      @user = FactoryGirl.create(:user)
            
      @user.comments << @comment
    end
    
    it "has a user attribute" do
      @comment.should respond_to(:user)
    end
    
    it "has the right user" do
      @comment.user.should == @user
    end
    
    it "doesn't destroy associated users" do
      @comment.destroy
      User.find_by(id: @user.id).should_not be_nil
    end
    
  end
  
  describe "commentable association" do
    
    before do
      @comment = Comment.create(@attr)
      
      @idea = FactoryGirl.create(:idea)
      @spark = FactoryGirl.create(:spark)
    end
    
    it "has a commentable attribute" do
      @comment.should respond_to(:commentable)
    end
    
    it "can be a spark" do
      expect {
        @comment.commentable = @spark
        @comment.save
      }.not_to raise_error
    end
    
    it "can be an idea" do
      expect {
        @comment.commentable = @idea
        @comment.save
      }.not_to raise_error
    end
    
    it "has the right commentable" do
      @comment.commentable = @spark
      @comment.save
      
      Comment.find(@comment.id).commentable.should == @spark
    end
    
    it "doesn't destroy associated commentable jawn" do
      @comment.commentable = @spark
      @comment.save
      
      @comment.destroy
      Spark.find_by(id: @spark.id).should_not be_nil
    end
    
  end
  
end
