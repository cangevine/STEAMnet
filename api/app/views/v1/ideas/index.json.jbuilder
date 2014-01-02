json.array! @ideas do |idea|
  json.partial! 'idea', idea: idea, lite: params[:lite] == "true"
end
