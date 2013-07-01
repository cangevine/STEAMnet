class CreateTags < ActiveRecord::Migration
  def change
    create_table :tags do |t|
      t.text :tag_text

      t.timestamps
    end
  end
end
