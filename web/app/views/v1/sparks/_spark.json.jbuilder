json.(spark, :id, :created_at, :spark_type, :content_type, :content, :content_hash)

json.users spark.users, :id, :name, :email
json.partial! 'v1/tags/tags', :tags => spark.tags
json.partial! 'v1/comments/comments', :comments => spark.comments
