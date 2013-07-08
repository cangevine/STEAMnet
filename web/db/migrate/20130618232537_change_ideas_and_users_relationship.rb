class ChangeIdeasAndUsersRelationship < ActiveRecord::Migration
  def up
    add_column :ideas, :user_id, :integer
    
    Idea.all.each do |idea|
      user = User.find_by_sql(["SELECT users.* FROM users INNER JOIN ideas_users ON users.id = ideas_users.user_id WHERE ideas_users.idea_id = ?", idea.id]).first
      
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
        ActiveRecord::Base.connection.execute("INSERT INTO ideas_users (idea_id, user_id) VALUES (#{idea.id}, #{user.id})")
      end
    end
    
    remove_columns :ideas, :user_id
  end
end
