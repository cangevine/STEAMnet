json.array! @tags do |tag|
  json.partial! 'tag', tag: tag
end
