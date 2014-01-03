class ChangeContentTypeAndSparkTypeLength < ActiveRecord::Migration
  def change
    change_column :sparks, :spark_type, :string, :limit => 1
    change_column :sparks, :content_type, :string, :limit => 1
  end
end
