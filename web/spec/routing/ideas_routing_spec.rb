require 'spec_helper'

describe "routes for Ideas" do
  
  it "routes get /api/v1/ideas.json to Ideas controller" do
    { :get => "/api/v1/ideas.json" }.should route_to(
      :controller => "v1/ideas",
      :action => "index",
      :format => "json"
    )
  end
  
  it "routes post /api/v1/ideas.json to Ideas controller" do
    { :post => "/api/v1/ideas.json" }.should route_to(
      :controller => "v1/ideas",
      :action => "create",
      :format => "json"
    )
  end
  
  it "routes get /api/v1/ideas/:id.json to Ideas controller" do
    { :get => "/api/v1/ideas/1.json" }.should route_to(
      :controller => "v1/ideas",
      :action => "show",
      :id => "1",
      :format => "json"
    )
  end
  
  it "routes delete /api/v1/ideas/:id.json to Ideas controller" do
    { :delete => "/api/v1/ideas/1.json" }.should route_to(
      :controller => "v1/ideas",
      :action => "destroy",
      :id => "1",
      :format => "json"
    )
  end
  
end