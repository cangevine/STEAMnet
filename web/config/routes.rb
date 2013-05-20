Steamnet::Application.routes.draw do
  
  namespace :api do
    
    namespace :v1 do
      
      resources :tags, :only => [:index, :show,]
      
      resources :comments, :except => [:new, :edit]
      
      resources :sparks, :except => [:new, :edit]
      
      resources :users, :except => [:new, :edit]
      
      resources :ideas, :except => [:new, :edit]
      
    end
    
  end
  
end
