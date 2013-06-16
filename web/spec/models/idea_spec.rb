# == Schema Information
#
# Table name: ideas
#
#  id          :integer          not null, primary key
#  description :text
#  created_at  :datetime         not null
#  updated_at  :datetime         not null
#

require 'spec_helper'

describe Idea do
  
  before(:each) do
    @attr = {
      :description  => "This is an idea"
    }
  end
  
  it "creates a new instance given valid attributes" do
    Idea.create!(@attr)
  end
  
  describe "spark association" do
    
    before(:each) do
      @idea = Idea.create(@attr)
      
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @s1.ideas << @idea
      @s2.ideas << @idea
    end
    
    it "has a sparks attribute" do
      @idea.should respond_to(:sparks)
    end
    
    it "has the right sparks" do
      @idea.sparks.should == [@s1, @s2]
    end
    
    it "doesn't destroy associated sparks" do
      @idea.destroy
      [@s1, @s2].each do |s|
        Spark.find_by_id(s.id).should_not be_nil
      end
    end
    
  end
  
  describe "user association" do
    
    before(:each) do
      @idea = Idea.create(@attr)
      
      @u1 = FactoryGirl.create(:user)
      @u2 = FactoryGirl.create(:user)
      
      @u1.ideas << @idea
      @u2.ideas << @idea
    end
    
    it "has a users attribute" do
      @idea.should respond_to(:users)
    end
    
    it "has the right users" do
      @idea.users.should == [@u1, @u2]
    end
    
    it "doesn't destroy associated users" do
      @idea.destroy
      [@u1, @u2].each do |u|
        User.find_by_id(u.id).should_not be_nil
      end
    end
    
  end
  
  describe "comment association" do
    
    before(:each) do
      @idea = Idea.create(@attr)
      
      @user = FactoryGirl.create(:user)
      
      @c1 = FactoryGirl.create(:comment)
      @c2 = FactoryGirl.create(:comment)
      
      @c1.user = @user
      @c2.user = @user
      
      @c1.commentable = @idea
      @c2.commentable = @idea
      
      @c1.save
      @c2.save
    end
    
    it "has an comments attribute" do
      @idea.should respond_to(:comments)
    end
    
    it "has the right comments" do
      @idea.comments.should == [@c1, @c2]
    end
    
    it "does destroy associated comments" do
      @idea.destroy
      [@c1, @c2].each do |c|
        Comment.find_by_id(c.id).should be_nil
      end
    end
    
  end
  
  describe "tag association" do
      
      before(:each) do
        @idea = FactoryGirl.create(:idea)
        
        @t1 = FactoryGirl.create(:tag)
        @t2 = FactoryGirl.create(:tag)
        
        @t1.ideas << @idea
        @t2.ideas << @idea
      end
      
      it "has a tags attribute" do
        @idea.should respond_to(:tags)
      end
      
      it "has the right tags" do
        @idea.tags.should == [@t1, @t2]
      end
      
      it "doesn't destroy associated tags" do
        @idea.destroy
        [@t1, @t2].each do |t|
          Tag.find_by_id(t.id).should_not be_nil
        end
      end
      
    end
  
end
