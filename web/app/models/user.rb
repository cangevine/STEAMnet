# == Schema Information
#
# Table name: users
#
#  id            :integer          not null, primary key
#  name          :string(255)
#  password_hash :text
#  email         :string(255)
#  created_at    :datetime         not null
#  updated_at    :datetime         not null
#

class User < ActiveRecord::Base
  attr_accessible :email, :name, :password_hash
  has_and_belongs_to_many :ideas
  has_and_belongs_to_many :sparks
end
