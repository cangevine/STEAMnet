class RemoveSparkId < ActiveRecord::Migration
  def change
  	remove_columns :comments, :spark_id 
  end
end
