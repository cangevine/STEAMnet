# == Schema Information
#
# Table name: authentications
#
#  id         :integer          not null, primary key
#  user_id    :integer
#  provider   :string(255)
#  uid        :string(255)
#  created_at :datetime
#  updated_at :datetime
#

require 'spec_helper'

describe Authentication do
  
  before do
    @attr = {
      :provider => "This is an idea",
      :uid      => "my@email.com"
    }
  end
  
  it "creates a new instance given valid attributes" do
    Authentication.create!(@attr)
  end
  
  describe "validation" do
    
    it "requires a provider" do
      auth = Authentication.new(@attr)
      auth.provider = ""
      auth.should_not be_valid
    end
    
    it "requires a uid" do
      auth = Authentication.new(@attr)
      auth.uid = ""
      auth.should_not be_valid
    end
    
  end
  
  describe "user association" do
    
    before do
      @auth = Authentication.create(@attr)
      
      @user = FactoryGirl.create(:user)
            
      @user.authentications << @auth
    end
    
    it "has a user attribute" do
      @auth.should respond_to(:user)
    end
    
    it "has the right user" do
      @auth.user.should == @user
    end
    
    it "doesn't destroy associated users" do
      @auth.destroy
      User.find_by(id: @user.id).should_not be_nil
    end
    
  end
  
end
