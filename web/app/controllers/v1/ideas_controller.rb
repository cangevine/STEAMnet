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
        if @idea.save
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
          
          if(params[:sparks])
            sparks = params[:sparks].split(",")
            
            sparks.each do |spark_id|
              spark = Spark.find(spark_id.to_i)
              
              if(spark)
                @idea.sparks << spark
              end
            end
          end
          
          user.ideas << @idea
          
          format.html { redirect_to @idea, :notice => 'Idea was successfully created.' }
          format.json { render :json => @idea, :status => :created, :location => ["v1", @idea] }
        else
          format.html { render :action => "new" }
          format.json { render :json => @idea.errors, :status => :unprocessable_entity }
        end
      else
        format.html { render :action => "new" }
        format.json { render :json => @idea.errors, :status => :unauthorized }
      end
    end
  end

  # PUT /ideas/1
  # PUT /ideas/1.json
  def update
    @idea = Idea.find(params[:id])

    respond_to do |format|
      if @idea.update_attributes(params[:idea])
        format.html { redirect_to @idea, :notice => 'Idea was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @idea.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /ideas/1
  # DELETE /ideas/1.json
  def destroy
    @idea = Idea.find(params[:id])
    @idea.destroy

    respond_to do |format|
      format.html { redirect_to ideas_url }
      format.json { head :no_content }
    end
  end
end
