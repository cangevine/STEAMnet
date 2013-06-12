require 'spec_helper'

describe "routes for Comments" do
  
  it "routes get /api/v1/comments.json to Comments controller" do
    { :get => "/api/v1/comments.json" }.should route_to(
      :controller => "v1/comments",
      :action => "index",
      :format => "json"
    )
  end
  
  it "routes post /api/v1/comments.json to Comments controller" do
    { :post => "/api/v1/comments.json" }.should route_to(
      :controller => "v1/comments",
      :action => "create",
      :format => "json"
    )
  end
  
  it "routes get /api/v1/comments/:id.json to Comments controller" do
    { :get => "/api/v1/comments/1.json" }.should route_to(
      :controller => "v1/comments",
      :action => "show",
      :id => "1",
      :format => "json"
    )
  end
  
  it "routes put /api/v1/comments/:id.json to Comments controller" do
    { :put => "/api/v1/comments/1.json" }.should route_to(
      :controller => "v1/comments",
      :action => "update",
      :id => "1",
      :format => "json"
    )
  end
  
  it "routes delete /api/v1/comments/:id.json to Comments controller" do
    { :delete => "/api/v1/comments/1.json" }.should route_to(
      :controller => "v1/comments",
      :action => "destroy",
      :id => "1",
      :format => "json"
    )
  end
  
end