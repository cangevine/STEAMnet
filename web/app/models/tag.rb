# == Schema Information
#
# Table name: tags
#
#  id         :integer          not null, primary key
#  tag_text   :string(255)
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

class Tag < ActiveRecord::Base
  def to_param
    tag_text
  end
  
  attr_accessible :tag_text
  
  has_many :sparks, :through => :tag_linkers, :source => :tagable, :source_type => "Spark"
  has_many :ideas, :through => :tag_linkers, :source => :tagable, :source_type => "Idea"
  has_many :tag_linkers
  
  validates :tag_text, :presence => true, :format => { :with => /^[A-Za-z\d_-]+$/, :message => "must be alphanumerical." }, :uniqueness => { :case_sensitive => true }
  
  def as_json(options={})
    super(:include => [:sparks, :ideas])
  end
end
