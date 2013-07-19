# == Schema Information
#
# Table name: users
#
#  id         :integer          not null, primary key
#  name       :string(255)
#  email      :string(255)
#  created_at :datetime         not null
#  updated_at :datetime         not null
#

class User < ActiveRecord::Base
  has_many :authentications, :dependent => :destroy
  has_many :devices, :dependent => :destroy
  
  has_and_belongs_to_many :sparks
  has_many :ideas
  has_many :comments
  
  validates :email, :allow_blank => true, :format => { :with => /\A[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]+\z/, :message => "must be a valid email address" }
  validates :name, :presence => true
end
