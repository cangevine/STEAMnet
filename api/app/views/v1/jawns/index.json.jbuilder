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
