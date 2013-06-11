require 'spec_helper'

describe "routes for users" do
  
  it "routes /profile/:username to profile#show for username" do
    expect(:get => "/profiles/jsmith").to route_to(
      :controller => "profiles",
      :action => "show",
      :username => "jsmith"
    )
  end
  
  it "does not expose a list of profiles" do
    expect(:get => "/profiles").not_to be_routable
  end
  
end