json.array! @sparks do |spark|
  json.partial! 'spark', spark: spark, lite: params["lite"] == "true"
end
