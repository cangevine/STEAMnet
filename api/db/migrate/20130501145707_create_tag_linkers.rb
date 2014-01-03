class CreateTagLinkers < ActiveRecord::Migration
  def change
    create_table :tag_linkers do |t|

      t.timestamps
    end
  end
end
