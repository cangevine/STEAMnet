if params[:lite] == "true"
  json.array! @jawns do |jawn|
    type = jawn.class.to_s.downcase
    
    case type
    when "spark"
      json.(jawn, :id, :spark_type, :content_type, :content, :content_hash, :created_at)
    when "idea"
      json.(jawn, :id, :description, :created_at)
    end

    json.jawn_type type
  end
else
  json.array! @jawns do |jawn|
    type = jawn.class.to_s.downcase

    case type
    when "spark"
      json.partial! 'v1/sparks/spark', spark: jawn
    when "idea"
      json.partial! 'v1/ideas/idea', idea: jawn
    end

    json.jawn_type type
  end
end