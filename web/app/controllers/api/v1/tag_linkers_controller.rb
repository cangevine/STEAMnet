class Api::V1::TagLinkersController < ApplicationController
  # GET /tag_linkers
  # GET /tag_linkers.json
  def index
    @tag_linkers = TagLinker.all

    respond_to do |format|
      format.html # index.html.erb
      format.json { render :json => @tag_linkers }
    end
  end

  # GET /tag_linkers/1
  # GET /tag_linkers/1.json
  def show
    @tag_linker = TagLinker.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render :json => @tag_linker }
    end
  end

  # GET /tag_linkers/new
  # GET /tag_linkers/new.json
  def new
    @tag_linker = TagLinker.new

    respond_to do |format|
      format.html # new.html.erb
      format.json { render :json => @tag_linker }
    end
  end

  # GET /tag_linkers/1/edit
  def edit
    @tag_linker = TagLinker.find(params[:id])
  end

  # POST /tag_linkers
  # POST /tag_linkers.json
  def create
    @tag_linker = TagLinker.new(params[:tag_linker])

    respond_to do |format|
      if @tag_linker.save
        format.html { redirect_to @tag_linker, :notice => 'Tag linker was successfully created.' }
        format.json { render :json => @tag_linker, :status => :created, :location => @tag_linker }
      else
        format.html { render :action => "new" }
        format.json { render :json => @tag_linker.errors, :status => :unprocessable_entity }
      end
    end
  end

  # PUT /tag_linkers/1
  # PUT /tag_linkers/1.json
  def update
    @tag_linker = TagLinker.find(params[:id])

    respond_to do |format|
      if @tag_linker.update_attributes(params[:tag_linker])
        format.html { redirect_to @tag_linker, :notice => 'Tag linker was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render :action => "edit" }
        format.json { render :json => @tag_linker.errors, :status => :unprocessable_entity }
      end
    end
  end

  # DELETE /tag_linkers/1
  # DELETE /tag_linkers/1.json
  def destroy
    @tag_linker = TagLinker.find(params[:id])
    @tag_linker.destroy

    respond_to do |format|
      format.html { redirect_to tag_linkers_url }
      format.json { head :no_content }
    end
  end
end
