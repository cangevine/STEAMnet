require 'spec_helper'

describe User do
  
  before(:each) do
    @attr = {
      :name     => "max",
      :email    => "max@example.com",
      :password => "testpassword",
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
    
    it "requires an email" do
      user = User.new(@attr)
      user.email = ""
      user.should_not be_valid
    end
    
    it "requires unique usernames" do
      user1 = User.new(@attr)
      user1.save
      
      user2 = User.new(@attr)
      user2.email = "test@something.com"
      
      user2.should_not be_valid
    end
    
    it "requires unique emails" do
      user1 = User.new(@attr)
      user1.save
      
      user2 = User.new(@attr)
      user2.name = "colin"
      
      user2.should_not be_valid
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
  
  describe "password" do
    
    describe "validation" do
      
      it "requires a password" do
        user = User.new(@attr)
        user.password = ""
        user.should_not be_valid
      end
      
    end
    
    describe "encryption" do
      
      before(:each) do
        @user = User.create!(@attr)
      end
      
      it "has a password digest attribute" do
        @user.should respond_to(:password_digest)
      end
      
      it "sets the encrypted password" do
        @user.password_digest.should_not be_blank
      end
      
    end
    
  end
  
  describe "spark associations" do
    
    before(:each) do
      
    end
    
  end
  
  describe "idea associations" do
    
    
    
  end
  
  describe "comment associations" do
    
    
    
  end
  
end