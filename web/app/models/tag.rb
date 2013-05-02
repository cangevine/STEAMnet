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
  has_many :tag_linkers
end
