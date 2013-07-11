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
  
  has_many :sparks, :through => :tag_linkers, :source => :tagable, :source_type => "Spark"
  has_many :ideas, :through => :tag_linkers, :source => :tagable, :source_type => "Idea"
  has_many :tag_linkers
  
  validates :tag_text, :presence => true, :format => { :with => /\A[A-Za-z\d_-]+\z/, :message => "must be alphanumerical" }, :uniqueness => { :case_sensitive => true }
  
  def jawns
    sparks = self.sparks.order("id DESC")
    ideas = self.ideas.order("id DESC")

    (sparks + ideas).sort_by(&:created_at).reverse
  end
end
