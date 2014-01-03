class AddFileToSparks < ActiveRecord::Migration
  def self.up
    add_attachment :sparks, :file
  end
  
  def self.down
    remove_attachment :sparks, :file
  end
end
