class AddLinkerTables < ActiveRecord::Migration
  def change
  	create_table :ideas_sparks, :id => false do |t|
      t.integer :idea_id
      t.integer :spark_id
    end
    create_table :ideas_users, :id => false do |t|
      t.integer :idea_id
      t.integer :user_id
    end
    create_table :sparks_users, :id => false do |t|
      t.integer :spark_id
      t.integer :user_id
    end
  end
end
