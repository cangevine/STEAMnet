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
  attr_accessible :email, :name, :password
  
  has_secure_password
  
  has_and_belongs_to_many :ideas
  has_and_belongs_to_many :sparks
  has_many :comments
  
  validates :email, :presence => true, :format => { :with => /\A[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]+\z/, :message => "Must be a valid email address." }
  validates :name, :presence => true
  validates :password, :presence => { :on => :create }
  
  def as_json(options={})
    super(:only => [:email, :name], :include => [:ideas, :sparks])
  end
end
