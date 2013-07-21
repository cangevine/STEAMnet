# == Schema Information
#
# Table name: tags
#
#  id         :integer          not null, primary key
#  tag_text   :string(255)
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

require 'spec_helper'

describe Tag do
  
  before do
    @attr = {
      :tag_text => "yellow"
    }
  end
  
  it "creates a new instance given valid attributes" do
    Tag.create!(@attr)
  end
  
  describe "validation" do
    
    it "requires tag text" do
      tag = Tag.new(@attr)
      tag.tag_text = ""
      tag.should_not be_valid
    end
    
    it "requires unique tag text" do
      tag1 = Tag.new(@attr)
      tag1.save
      
      tag2 = Tag.new(@attr)
      tag2.tag_text = "yellow"
      
      tag2.should_not be_valid
    end
    
    it "accepts valid tag text" do
      tags = %w[purple red_heart nintendo64 some-tag]
      tags.each do |a|
        tag = Tag.new(@attr.merge(:tag_text => a))
        tag.should be_valid
      end
    end
    
    it "rejects invalid tag text" do
      tags = %w[@some,thing #yoloswag this:awesome]
      tags.push "this is cool"
      tags.each do |a|
        tag = Tag.new(@attr.merge(:tag_text => a))
        tag.should_not be_valid
      end
    end
    
  end
  
  describe "sparks association" do
    
    before do
      @tag = Tag.create(@attr)
      
      @s1 = FactoryGirl.create(:spark)
      @s2 = FactoryGirl.create(:spark)
      
      @s1.tags << @tag
      @s2.tags << @tag
    end
    
    it "has a sparks attribute" do
      @tag.should respond_to(:sparks)
    end
    
    it "has the right sparks" do
      @tag.sparks.should == [@s1, @s2]
    end
    
    it "doesn't destroy associated sparks" do
      @tag.destroy
      [@s1, @s2].each do |s|
        Spark.find_by(id: s.id).should_not be_nil
      end
    end
    
  end
  
  describe "ideas association" do
    
    before do
      @tag = Tag.create(@attr)
      
      @i1 = FactoryGirl.create(:idea)
      @i2 = FactoryGirl.create(:idea)
      
      @i1.tags << @tag
      @i2.tags << @tag
    end
    
    it "has an ideas attribute" do
      @tag.should respond_to(:ideas)
    end
    
    it "has the right ideas" do
      @tag.ideas.should == [@i1, @i2]
    end
    
    it "doesn't destroy associated ideas" do
      @tag.destroy
      [@i1, @i2].each do |i|
        Idea.find_by(id: i.id).should_not be_nil
      end
    end
    
  end
  
  describe "jawns helper" do
    
    before do
      @tag = Tag.create(@attr)
      
      @jawns = []
      
      20.times do
        if [true, false].sample
          jawn = FactoryGirl.create(:idea)
        else
          jawn = FactoryGirl.create(:spark)
        end
        
        jawn.tags << @tag
        @jawns << jawn
      end
      
      @jawns.reverse!
    end
    
    it "has a jawns method" do
      @tag.should respond_to(:jawns)
    end
    
    it "returns the correct jawns" do
      @tag.jawns.should == @jawns
    end
    
  end
  
end
