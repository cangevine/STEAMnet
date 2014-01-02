json.(spark, :id, :created_at, :spark_type, :content_type, :content, :content_hash)
json.file spark.file.url if spark.file.exists?

unless lite
  json.users spark.users, :id, :name, :email

  json.ideas spark.ideas, :id, :created_at, :description

  json.comments spark.comments do |comment|
    json.(comment, :id, :comment_text, :created_at)
    json.user comment.user, :id, :name, :email
  end

  tag_names = spark.tags.map(&:tag_text)
  json.tags tag_names
end
