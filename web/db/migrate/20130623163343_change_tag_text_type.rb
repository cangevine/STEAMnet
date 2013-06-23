class ChangeTagTextType < ActiveRecord::Migration
  def change
    change_column :tags, :tag_text, :string
  end
end
