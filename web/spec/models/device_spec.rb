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

require 'spec_helper'

describe Device do
  pending "add some examples to (or delete) #{__FILE__}"
end
