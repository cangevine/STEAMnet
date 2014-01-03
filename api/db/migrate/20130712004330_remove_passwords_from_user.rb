class RemovePasswordsFromUser < ActiveRecord::Migration
  def change
    remove_column :users, :password_digest, :text
  end
end
