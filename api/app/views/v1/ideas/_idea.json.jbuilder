json.(idea, :id, :created_at, :description)

unless lite
  json.user idea.user, :id, :name, :email if idea.user

  json.sparks idea.sparks do |spark|
    json.(spark, :id, :created_at, :spark_type, :content_type, :content, :content_hash)
    json.file spark.file.url if spark.file.exists?
  end

  json.comments idea.comments do |comment|
    json.(comment, :id, :comment_text, :created_at)
    json.user comment.user, :id, :name, :email
  end

  tag_names = idea.tags.map(&:tag_text)
  json.tags tag_names
end
