class V1::IdeasController < ApplicationController
  # GET /ideas
  # GET /ideas.json
  def index
    if params[:limit]
      @ideas = Idea.order("id DESC").limit(params[:limit])
    else
      @ideas = Idea.order("id DESC")
    end

    respond_to do |format|
      format.html # index.html.erb
      format.json { render :json => @ideas }
    end
  end

  # GET /ideas/1
  # GET /ideas/1.json
  def show
    @idea = Idea.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => @idea }
    end
  end
  
  # POST /ideas
  # POST /ideas.json
  def create
    @idea = Idea.new(params[:idea])
    
    respond_to do |format|
      user = User.find_by_name(params[:username])
      
      if user
        if params[:sparks]
          spark_ids = params[:sparks].split(",")
          sparks = []
        
          sparks_exist = false

          spark_ids.each do |spark_id|
            if s = Spark.find_by_id(spark_id.to_i)
              sparks_exist = true
              sparks << s
            end
          end
        
          if sparks_exist && @idea.save
            if(params[:tags])
              tags = params[:tags].split(",")
            
              tags.each do |tag_name|
                tag = Tag.where(:tag_text => tag_name).first
              
                unless(tag)
                  tag = Tag.new(:tag_text => tag_name)
                  tag.save
                end
              
                tag.ideas << @idea
              end
            end
          
            sparks.each do |spark|
              @idea.sparks << spark
            end
          
            user.ideas << @idea
            
            format.html { redirect_to @idea, :notice => 'Idea was successfully created.' }
            format.json { render :json => @idea, :status => :created, :location => ["v1", @idea] }
          else # Either no valid sparks were passed or the idea didn't save
            format.html { render :action => "new" }
            format.json { render :json => @idea.errors, :status => :unprocessable_entity }
          end
        else # No sparks passed in
          format.html { render :action => "new" }
          format.json { render :json => @idea.errors, :status => :unprocessable_entity }
        end
      else # User doesn't exist or wasn't passed in
        format.html { render :action => "new" }
        format.json { render :json => @idea.errors, :status => :unauthorized }
      end
    end
  end

  # DELETE /ideas/1
  # DELETE /ideas/1.json
  def destroy
    @idea = Idea.find(params[:id])
    user = User.find_by_name(params[:username])
    
    if @idea && user && @idea.users.include?(user)
      @idea.users.delete(user)
      
      respond_to do |format|
        format.html { redirect_to ideas_url }
        format.json { render :json => @idea, :status => :ok }
      end
    end
  end
end
