# == Schema Information
#
# Table name: sparks
#
#  id           :integer          not null, primary key
#  spark_type   :string(255)
#  content_type :string(255)
#  content      :text
#  content_hash :text
#  created_at   :datetime         not null
#  updated_at   :datetime         not null
#

require 'test_helper'

class SparkTest < ActiveSupport::TestCase
  # test "the truth" do
  #   assert true
  # end
end
