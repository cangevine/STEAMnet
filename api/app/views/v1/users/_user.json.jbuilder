json.(user, :id, :name, :email, :created_at, :updated_at)

json.sparks user.sparks do |spark|
  json.(spark, :id, :created_at, :spark_type, :content_type, :content, :content_hash)
  json.file spark.file.url if spark.file.exists?
end
json.ideas user.ideas, :id, :created_at, :description
