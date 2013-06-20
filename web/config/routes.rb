Steamnet::Application.routes.draw do
  
  scope "api" do
    
    namespace :v1 do
      
      resources :tags, :only => [:index, :show]
      # resources :comments, :except => [:new, :edit]
      resources :sparks, :except => [:new, :edit, :update] do
        resources :comments, :except => [:new, :edit, :update]
      end
      resources :users, :except => [:new, :edit]
      resources :ideas, :except => [:new, :edit, :update] do
        resources :comments, :except => [:new, :edit, :update]
      end
      
      get "jawns" => "jawns#index"
      
    end
    
  end
  
end

#== Route Map
# Generated on 16 Jun 2013 11:41
#
#      v1_tag GET    /api/v1/tags/:id(.:format)     v1/tags#show
# v1_comments GET    /api/v1/comments(.:format)     v1/comments#index
#             POST   /api/v1/comments(.:format)     v1/comments#create
#  v1_comment GET    /api/v1/comments/:id(.:format) v1/comments#show
#             PUT    /api/v1/comments/:id(.:format) v1/comments#update
#             DELETE /api/v1/comments/:id(.:format) v1/comments#destroy
#   v1_sparks GET    /api/v1/sparks(.:format)       v1/sparks#index
#             POST   /api/v1/sparks(.:format)       v1/sparks#create
#    v1_spark GET    /api/v1/sparks/:id(.:format)   v1/sparks#show
#             DELETE /api/v1/sparks/:id(.:format)   v1/sparks#destroy
#    v1_users GET    /api/v1/users(.:format)        v1/users#index
#             POST   /api/v1/users(.:format)        v1/users#create
#     v1_user GET    /api/v1/users/:id(.:format)    v1/users#show
#             PUT    /api/v1/users/:id(.:format)    v1/users#update
#             DELETE /api/v1/users/:id(.:format)    v1/users#destroy
#    v1_ideas GET    /api/v1/ideas(.:format)        v1/ideas#index
#             POST   /api/v1/ideas(.:format)        v1/ideas#create
#     v1_idea GET    /api/v1/ideas/:id(.:format)    v1/ideas#show
#             DELETE /api/v1/ideas/:id(.:format)    v1/ideas#destroy
#    v1_jawns GET    /api/v1/jawns(.:format)        v1/jawns#index
