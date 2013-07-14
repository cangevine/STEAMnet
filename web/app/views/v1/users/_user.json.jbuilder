json.(user, :id, :name, :email, :created_at, :updated_at)

json.sparks user.sparks, :id, :created_at, :spark_type, :content_type, :content, :content_hash
json.ideas user.ideas, :id, :created_at, :description
