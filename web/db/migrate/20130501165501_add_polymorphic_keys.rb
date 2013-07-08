class AddPolymorphicKeys < ActiveRecord::Migration
  def change
  	add_column :tag_linkers, :tagable_id, :integer
  	add_column :tag_linkers, :tagable_type, :string
  	add_column :tag_linkers, :tag_id, :integer
  end
end
