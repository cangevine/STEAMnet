class CreateSparks < ActiveRecord::Migration
  def change
    create_table :sparks do |t|
      t.string :spark_type
      t.string :content_type
      t.text :content
      t.text :content_hash

      t.timestamps
    end
  end
end
