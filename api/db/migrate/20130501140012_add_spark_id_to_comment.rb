class AddSparkIdToComment < ActiveRecord::Migration
  def change
    add_column :comments, :spark_id, :integer
  end
end
