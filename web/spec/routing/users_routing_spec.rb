require 'spec_helper'

describe "routes for Users" do
  
  it "routes get /api/v1/users.json to Users controller" do
    { :get => "/api/v1/users.json" }.should route_to(
      :controller => "v1/users",
      :action => "index",
      :format => "json"
    )
  end
  
  it "routes get /api/v1/users/:id.json to Users controller" do
    { :get => "/api/v1/users/max.json" }.should route_to(
      :controller => "v1/users",
      :action => "show",
      :id => "max",
      :format => "json"
    )
  end
  
  it "routes patch /api/v1/users/:id.json to Users controller" do
    { :patch => "/api/v1/users/max.json" }.should route_to(
      :controller => "v1/users",
      :action => "update",
      :id => "max",
      :format => "json"
    )
  end
  
  it "routes delete /api/v1/users/:id.json to Users controller" do
    { :delete => "/api/v1/users/max.json" }.should route_to(
      :controller => "v1/users",
      :action => "destroy",
      :id => "max",
      :format => "json"
    )
  end
  
end