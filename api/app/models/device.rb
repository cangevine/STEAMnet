# == Schema Information
#
# Table name: devices
#
#  id              :integer          not null, primary key
#  token           :string(255)
#  registration_id :string(255)
#  created_at      :datetime
#  updated_at      :datetime
#  user_id         :integer
#

class Device < ActiveRecord::Base
  belongs_to :user
  
  validates :token, :uniqueness => true
  
  before_create :generate_token
  
  private
  
    def generate_token
      begin
        self.token = SecureRandom.hex
      end while self.class.exists?(token: token)
    end
end
