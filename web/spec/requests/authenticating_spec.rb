require 'spec_helper'

describe "Authenticating" do
  
  before do
    # request.env["omniauth.auth"] = OmniAuth.config.mock_auth[:developer] 
  end
  
  it "should create a new authentication" do
    expect {
      visit "/auth/developer"
    }.to change { Authentication.count }.by(1)
  end
  
  it "should create a new user" do
    expect {
      visit "/auth/developer"
    }.to change { User.count }.by(1)
  end
  
  describe "with an existing user and authentication" do
    
    before do
      @user = FactoryGirl.create(:user)
      @user.authentications.create(:provider => "developer", :uid => "my@email.com")
    end
    
    it "shouldn't create a new authentication" do
      expect {
        visit "/auth/developer"
      }.not_to change { Authentication.count }.by(1)
    end
    
    it "shouldn't create a new user" do
      expect {
        visit "/auth/developer"
      }.not_to change { Authentication.count }.by(1)
    end
    
  end

end
