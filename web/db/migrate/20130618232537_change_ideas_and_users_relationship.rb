class ChangeIdeasAndUsersRelationship < ActiveRecord::Migration
  def up
    add_column :ideas, :user_id, :integer
    
    Idea.all.each do |idea|
      user = idea.users.first
      
      if user
        idea.user = user
        idea.save
      end
    end
    
    drop_table :ideas_users
  end

  def down
    create_table :ideas_users, :id => false do |t|
      t.integer :idea_id
      t.integer :user_id
    end
    
    Idea.all.each do |idea|
      user = idea.user
      
      if user
        idea.users << user
      end
    end
    
    remove_columns :ideas, :user_id
  end
end
