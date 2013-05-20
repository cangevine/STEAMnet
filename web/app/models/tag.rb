# == Schema Information
#
# Table name: tags
#
#  id         :integer          not null, primary key
#  tag_text   :text
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

class Tag < ActiveRecord::Base
  attr_accessible :tag_text
  
  has_many :sparks, :through => :tag_linkers, :source => :tagable, :source_type => "Spark"
  has_many :ideas, :through => :tag_linkers, :source => :tagable, :source_type => "Idea"
  has_many :tag_linkers
  
  validates :tag_text, :presence => true
  
  def as_json(options={})
    super(:include => [:sparks, :ideas])
  end
end
