Steamnet::Application.routes.draw do
  
  namespace :api do
    
    namespace :v1 do
      
      resources :tag_linkers
      
      resources :tags
      
      resources :comments
      
      resources :sparks
      
      resources :users
      
      resources :ideas
      
    end
    
  end
  
end
