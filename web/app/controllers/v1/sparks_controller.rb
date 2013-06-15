class V1::SparksController < ApplicationController
  # GET /sparks
  # GET /sparks.json
  def index
    if params[:limit]
      @sparks = Spark.order("id DESC").limit(params[:limit])
    else
      @sparks = Spark.order("id DESC")
    end
    
    respond_to do |format|
      format.html # index.html.erb
      format.json { render :json => @sparks }
    end
  end

  # GET /sparks/1
  # GET /sparks/1.json
  def show
    @spark = Spark.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => @spark }
    end
  end
  
  # POST /sparks
  # POST /sparks.json
  def create
    @spark = Spark.new(params[:spark])
    
    respond_to do |format|
      user = User.find_by_name(params[:username])
      
      if user
        if @spark.save
          if(params[:tags])
            tags = params[:tags].split(",")
            
            tags.each do |tag_name|
              tag = Tag.where(:tag_text => tag_name).first
              
              unless(tag)
                tag = Tag.new(:tag_text => tag_name)
                tag.save
              end
              
              tag.sparks << @idea
            end
          end
          
          user.sparks << @spark
        
          format.html { redirect_to @spark, :notice => 'Spark was successfully created.' }
          format.json { render :json => @spark, :status => :created, :location => ["v1", @spark] }
        elsif @spark.duplicate?
          @spark = Spark.find_by_content_hash(@spark.content_hash)
          
          unless(@spark.users.include?(user))
            @spark.users << user
          end
          
          format.html { render :action => "new" }
          format.json { render :json => @spark, :status => :accepted }
        else
          format.html { render :action => "new" }
          format.json { render :json => @spark.errors, :status => :unprocessable_entity }
        end
      else
        format.html { render :action => "new" }
        format.json { render :json => @spark.errors, :status => :unauthorized }
      end
    end
  end

  # PUT /sparks/1
  # PUT /sparks/1.json
  def update
    @spark = Spark.find(params[:id])
    
    respond_to do |format|
      if @spark.update_attributes(params[:spark])
        format.html { redirect_to @spark, :notice => 'Spark was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @spark.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /sparks/1
  # DELETE /sparks/1.json
  def destroy
    @spark = Spark.find(params[:id])
    @spark.destroy

    respond_to do |format|
      format.html { redirect_to sparks_url }
      format.json { head :no_content }
    end
  end
end
