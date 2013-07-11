# == Schema Information
#
# Table name: comments
#
#  id               :integer          not null, primary key
#  comment_text     :text
#  user_id          :integer
#  created_at       :datetime         not null
#  updated_at       :datetime         not null
#  commentable_id   :integer
#  commentable_type :string(255)
#

class Comment < ActiveRecord::Base
  belongs_to :user
  belongs_to :commentable, :polymorphic => true
  
  validates :comment_text, :presence => true
end
