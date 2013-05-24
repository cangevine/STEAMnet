# == Schema Information
#
# Table name: sparks
#
#  id           :integer          not null, primary key
#  spark_type   :string(255)
#  content_type :string(255)
#  content      :text
#  content_hash :text
#  created_at   :datetime         not null
#  updated_at   :datetime         not null
#

class Spark < ActiveRecord::Base
  attr_accessible :content, :content_hash, :content_type, :spark_type
  
  has_and_belongs_to_many :ideas
  has_and_belongs_to_many :users
  has_many :comments, :as => :commentable
  has_many :tags, :through => :tag_linkers
  has_many :tag_linkers, :as => :tagable
  
  # validates :content, :presence => true
  # validates :content_hash, :presence => true
  validates :content_type, :presence => true
  validates :spark_type, :presence => true
  
  def as_json(options={})
    super(:include => [:tags, :comments, :users])
  end
end
