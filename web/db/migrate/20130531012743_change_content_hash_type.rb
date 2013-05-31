class ChangeContentHashType < ActiveRecord::Migration
  def change
    change_column :sparks, :content_hash, :string
  end
end
