# if params[:lite] == "true"
#   json.array! @jawns do |jawn|
#     type = jawn.class.to_s.downcase
#     
#     case type
#     when "spark"
#       json.(jawn, :id, :created_at, :spark_type, :content_type, :content, :content_hash)
#       json.file jawn.file.url if jawn.file.exists?
#     when "idea"
#       json.(jawn, :id, :description, :created_at)
#     end
#     
#     json.jawn_type type
#   end
# else
  lite = params[:lite] == "true"
  
  json.array! @jawns do |jawn|
    type = jawn.class.to_s.downcase
    
    case type
    when "spark"
      json.partial! 'v1/sparks/spark', spark: jawn, lite: lite
    when "idea"
      json.partial! 'v1/ideas/idea', idea: jawn, lite: lite
    end
    
    json.jawn_type type
  end
# end