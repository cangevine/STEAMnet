# == Schema Information
#
# Table name: sparks
#
#  id           :integer          not null, primary key
#  spark_type   :string(255)
#  content_type :string(255)
#  content      :text
#  content_hash :string(255)
#  created_at   :datetime         not null
#  updated_at   :datetime         not null
#

require 'digest/sha1'

class Spark < ActiveRecord::Base
  attr_accessible :content, :content_hash, :content_type, :spark_type
  
  has_and_belongs_to_many :ideas
  has_and_belongs_to_many :users
  has_many :comments, :as => :commentable, :dependent => :destroy
  has_many :tags, :through => :tag_linkers
  has_many :tag_linkers, :as => :tagable
  
  validates :content_type, :presence => true
  validates :content, :presence => true
  validates :content_hash, :presence => true, :uniqueness => true
  validates :spark_type, :presence => true
  
  before_validation :hash_content
  
  def as_json(options={})
    json = super(:include => [:tags, :comments, :users])
    json[:jawn_type] = "spark"
    return json
  end
  
  def duplicate?
    return (self.errors.to_hash.length == 1) && (self.errors.to_hash.keys[0] == :content_hash)
  end
  
  private
  
    def hash_content
      self.content_hash = Digest::SHA1.hexdigest(self.content_type+"-"+self.content)
    end
    
end
