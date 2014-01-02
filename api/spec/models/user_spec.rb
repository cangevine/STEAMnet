# == Schema Information
#
# Table name: users
#
#  id         :integer          not null, primary key
#  name       :string(255)
#  email      :string(255)
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

require 'spec_helper'

describe User do
  
  before do
    @attr = {
      :name   => "max",
      :email  => "max@example.com"
    }
  end
  
  it "creates a new instance given valid attributes" do
    User.create!(@attr)
  end
  
  describe "validation" do
    
    it "requires a name" do
      user = User.new(@attr)
      user.name = ""
      user.should_not be_valid
    end
    
    it "accepts valid emails" do
      emails = %w[user@foo.com THE_USER@foo.bar.org first.last@foo.jp]
      emails.each do |a|
        user = User.new(@attr.merge(:email => a))
        user.should be_valid
      end
    end
    
    it "rejects invalid emails" do
      emails = %w[user@foo,com user_at_foo.org example.user@foo.]
      emails.each do |a|
        user = User.new(@attr.merge(:email => a))
        user.should_not be_valid
      end
    end
    
  end
  
  describe "spark association" do
    
    before do
      @user = User.create(@attr)
      
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @s1.users << @user
      @s2.users << @user
    end
    
    it "has a sparks attribute" do
      @user.should respond_to(:sparks)
    end
    
    it "has the right sparks" do
      @user.sparks.should == [@s1, @s2]
    end
    
    it "doesn't destroy associated sparks" do
      @user.destroy
      [@s1, @s2].each do |s|
        Spark.find_by(id: s.id).should_not be_nil
      end
    end
    
  end
  
  describe "idea association" do
    
    before do
      @user = User.create(@attr)
      
      @i1 = FactoryGirl.create(:idea)
      @i2 = FactoryGirl.create(:idea)
      
      @i1.user = @user
      @i2.user = @user
      
      @i1.save
      @i2.save
    end
    
    it "has an ideas attribute" do
      @user.should respond_to(:ideas)
    end
    
    it "has the right ideas" do
      @user.ideas.should == [@i1, @i2]
    end
    
    it "doesn't destroy associated ideas" do
      @user.destroy
      [@i1, @i2].each do |i|
        Idea.find_by(id: i.id).should_not be_nil
      end
    end
    
  end
  
  describe "comment association" do
    
    before do
      @user = User.create(@attr)
      
      @s = FactoryGirl.create(:spark)
      @i = FactoryGirl.create(:idea)
      
      @c1 = FactoryGirl.create(:comment)
      @c2 = FactoryGirl.create(:comment)
      
      @c1.commentable = @s
      @c2.commentable = @i
      
      @c1.user = @user
      @c2.user = @user
      
      @c1.save
      @c2.save
    end
    
    it "has an comments attribute" do
      @user.should respond_to(:comments)
    end
    
    it "has the right comments" do
      @user.comments.should == [@c1, @c2]
    end
    
    it "doesn't destroy associated comments" do
      @user.destroy
      [@c1, @c2].each do |c|
        Comment.find_by(id: c.id).should_not be_nil
      end
    end
    
  end
  
  describe "authentication association" do
    
    before do
      @user = User.create(@attr)
      
      @a1 = FactoryGirl.create(:authentication)
      @a2 = FactoryGirl.create(:authentication)
      
      @a1.user = @user
      @a2.user = @user
      
      @a1.save
      @a2.save
    end
    
    it "has an authentications attribute" do
      @user.should respond_to(:authentications)
    end
    
    it "has the right authentications" do
      @user.authentications.should == [@a1, @a2]
    end
    
    it "does destroy associated authentications" do
      @user.destroy
      [@a1, @a2].each do |a|
        Authentication.find_by(id: a.id).should be_nil
      end
    end
    
  end
  
  describe "device association" do
    
    before do
      @user = User.create(@attr)
      
      @d1 = FactoryGirl.create(:device)
      @d2 = FactoryGirl.create(:device)
      
      @d1.user = @user
      @d2.user = @user
      
      @d1.save
      @d2.save
    end
    
    it "has a devices attribute" do
      @user.should respond_to(:devices)
    end
    
    it "has the right devices" do
      @user.devices.should == [@d1, @d2]
    end
    
    it "does destroy associated devices" do
      @user.destroy
      [@d1, @d2].each do |d|
        Device.find_by(id: d.id).should be_nil
      end
    end
    
  end
  
end
