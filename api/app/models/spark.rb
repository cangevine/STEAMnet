# == Schema Information
#
# Table name: sparks
#
#  id                :integer          not null, primary key
#  spark_type        :string(1)
#  content_type      :string(1)
#  content           :text
#  content_hash      :string(255)
#  created_at        :datetime         not null
#  updated_at        :datetime         not null
#  file_file_name    :string(255)
#  file_content_type :string(255)
#  file_file_size    :integer
#  file_updated_at   :datetime
#

require 'digest/sha1'

class Spark < ActiveRecord::Base
  has_attached_file :file
  
  has_and_belongs_to_many :ideas
  has_and_belongs_to_many :users
  has_many :comments, :as => :commentable, :dependent => :destroy
  has_many :tags, :through => :tag_linkers
  has_many :tag_linkers, :as => :tagable
  
  validates :content_type, :presence => true, :format => { :with => /\A[LVCTPA]\z/, :message => "must be a valid content type" }
  validates :content, :presence => true
  validates :content_hash, :presence => true, :uniqueness => true
  validates :spark_type, :presence => true, :format => { :with => /\A[WIP]\z/, :message => "must be a valid spark type" }
  
  before_validation :hash_content
  
  def self.random(seed)
    self.connection.execute "select setseed(#{seed})"
    return self.order("RANDOM()")
  end
  
  def duplicate?
    return (self.errors.to_hash.length == 1) && (self.errors.to_hash.keys[0] == :content_hash)
  end
  
  private
  
    def hash_content
      if (self.content && self.content_type)
        self.content_hash = Digest::SHA1.hexdigest(self.content_type+"-"+self.content)
      end
    end
    
end
