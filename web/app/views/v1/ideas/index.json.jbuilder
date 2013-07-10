json.array! @ideas do |idea|
  json.partial! 'idea', idea: idea
end
