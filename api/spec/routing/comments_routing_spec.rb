require 'spec_helper'

describe "routes for Comments" do
  
  describe "on Sparks" do
    
    it "routes get /api/v1/sparks/:spark_id/comments.json to Comments controller" do
      { :get => "/api/v1/sparks/1/comments.json" }.should route_to(
        :controller => "v1/comments",
        :action => "index",
        :spark_id => "1",
        :format => "json"
      )
    end

    it "routes post /api/v1/sparks/:spark_id/comments.json to Comments controller" do
      { :post => "/api/v1/sparks/1/comments.json" }.should route_to(
        :controller => "v1/comments",
        :action => "create",
        :spark_id => "1",
        :format => "json"
      )
    end

    it "routes get /api/v1/sparks/:spark_id/comments/:id.json to Comments controller" do
      { :get => "/api/v1/sparks/1/comments/1.json" }.should route_to(
        :controller => "v1/comments",
        :action => "show",
        :spark_id => "1",
        :id => "1",
        :format => "json"
      )
    end
    
    it "routes delete /api/v1/sparks/:spark_id/comments/:id.json to Comments controller" do
      { :delete => "/api/v1/sparks/1/comments/1.json" }.should route_to(
        :controller => "v1/comments",
        :action => "destroy",
        :spark_id => "1",
        :id => "1",
        :format => "json"
      )
    end
    
  end
  
  describe "on Ideas" do
    
    it "routes get /api/v1/ideas/:idea_id/comments.json to Comments controller" do
      { :get => "/api/v1/ideas/1/comments.json" }.should route_to(
        :controller => "v1/comments",
        :action => "index",
        :idea_id => "1",
        :format => "json"
      )
    end

    it "routes post /api/v1/ideas/:idea_id/comments.json to Comments controller" do
      { :post => "/api/v1/ideas/1/comments.json" }.should route_to(
        :controller => "v1/comments",
        :action => "create",
        :idea_id => "1",
        :format => "json"
      )
    end

    it "routes get /api/v1/ideas/:idea_id/comments/:id.json to Comments controller" do
      { :get => "/api/v1/ideas/1/comments/1.json" }.should route_to(
        :controller => "v1/comments",
        :action => "show",
        :idea_id => "1",
        :id => "1",
        :format => "json"
      )
    end

    it "routes delete /api/v1/ideas/:idea_id/comments/:id.json to Comments controller" do
      { :delete => "/api/v1/ideas/1/comments/1.json" }.should route_to(
        :controller => "v1/comments",
        :action => "destroy",
        :idea_id => "1",
        :id => "1",
        :format => "json"
      )
    end
    
  end
  
end