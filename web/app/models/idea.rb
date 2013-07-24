# == Schema Information
#
# Table name: ideas
#
#  id          :integer          not null, primary key
#  description :text
#  created_at  :datetime         not null
#  updated_at  :datetime         not null
#  user_id     :integer
#

class Idea < ActiveRecord::Base
  belongs_to :user
  has_and_belongs_to_many :sparks
  has_many :comments, :as => :commentable, :dependent => :destroy
  has_many :tags, :through => :tag_linkers
  has_many :tag_linkers, :as => :tagable
  
  def self.random(seed)
    self.connection.execute "select setseed(#{seed})"
    return self.order("RANDOM()")
  end
end
