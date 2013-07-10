json.comments comments do |comment|
  json.(comment, :comment_text, :created_at)
  json.user comment.user, :id, :name, :email
end