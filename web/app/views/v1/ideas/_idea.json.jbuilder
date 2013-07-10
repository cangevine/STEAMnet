json.(idea, :id, :created_at, :description)

json.user idea.user, :id, :name, :email if idea.user

json.sparks idea.sparks, :id, :created_at, :spark_type, :content_type, :content, :content_hash

json.comments idea.comments do |comment|
  json.(comment, :comment_text, :created_at)
  json.user comment.user, :id, :name, :email
end

tag_names = idea.tags.map(&:tag_text)
json.tags tag_names
