require 'spec_helper'

describe "routes for Sparks" do
  
  it "routes get /api/v1/sparks.json to Sparks controller" do
    { :get => "/api/v1/sparks.json" }.should route_to(
      :controller => "v1/sparks",
      :action => "index",
      :format => "json"
    )
  end
  
  it "routes post /api/v1/sparks.json to Sparks controller" do
    { :post => "/api/v1/sparks.json" }.should route_to(
      :controller => "v1/sparks",
      :action => "create",
      :format => "json"
    )
  end
  
  it "routes get /api/v1/sparks/:id.json to Sparks controller" do
    { :get => "/api/v1/sparks/1.json" }.should route_to(
      :controller => "v1/sparks",
      :action => "show",
      :id => "1",
      :format => "json"
    )
  end
  
  it "routes delete /api/v1/sparks/:id.json to Sparks controller" do
    { :delete => "/api/v1/sparks/1.json" }.should route_to(
      :controller => "v1/sparks",
      :action => "destroy",
      :id => "1",
      :format => "json"
    )
  end
  
end