# == Schema Information
#
# Table name: ideas
#
#  id          :integer          not null, primary key
#  description :text
#  created_at  :datetime         not null
#  updated_at  :datetime         not null
#

class Idea < ActiveRecord::Base
  attr_accessible :description
  has_and_belongs_to_many :sparks
  has_and_belongs_to_many :users
end
