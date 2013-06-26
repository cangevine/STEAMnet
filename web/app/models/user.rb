# == Schema Information
#
# Table name: users
#
#  id              :integer          not null, primary key
#  name            :string(255)
#  password_digest :text
#  email           :string(255)
#  created_at      :datetime         not null
#  updated_at      :datetime         not null
#

class User < ActiveRecord::Base
  def to_param
    name
  end
  
  attr_accessible :email, :name, :password
  
  has_secure_password
  
  has_and_belongs_to_many :sparks
  has_many :ideas
  has_many :comments
  
  validates :email, :presence => true, :format => { :with => /\A[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]+\z/, :message => "must be a valid email address." }, :uniqueness => { :case_sensitive => false }
  validates :name, :presence => true, :format => { :with => /^[A-Za-z\d_-]+$/, :message => "must be alphanumerical." }, :uniqueness => { :case_sensitive => false }
  validates :password, :presence => { :on => :create }
  
  def as_json(options={})
    super(:only => [:created_at, :updated_at, :email, :name], :include => [:ideas, :sparks])
  end
end
