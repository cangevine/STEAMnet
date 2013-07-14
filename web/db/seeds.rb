# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).

require 'faker'

names = ["Max", "Colin Roberts", "Aqeel Philips", "Sam Beckley", "Drew Leventhal", "Dan Stadtmauer", "Heather Witzel-Lakin", "Grace Heard"]
users = []

names.each do |n|
  users << User.create(:name => n, :email => Faker::Internet.email)
end

tag_texts = %w[awesome purple yellow internet legit tablets android rails]
tags = []

tag_texts.each do |t|
  tags << Tag.create(:tag_text => t)
end

content_types = %w[L C T P A V]
spark_types = %w[W I P]

pictures = %w[http://1.bp.blogspot.com/_mLv-KWFqzEU/TOoJBVb7NJI/AAAAAAAAABk/cj44CcZq6v8/s1600/sparks%2525205.jpg http://upload.wikimedia.org/wikipedia/commons/4/41/Space_Shuttle_Columbia_launching.jpg http://images2.fanpop.com/images/photos/5800000/happy-kitten-kittens-5890512-1600-1200.jpg]
videos = %w[http://www.youtube.com/watch?v=j5C6X9vOEkU http://www.youtube.com/watch?v=1VuMdLm0ccU http://www.youtube.com/watch?v=oHg5SJYRHA0]
audios = %w[http://soundcloud.com/se-beat/se-beat-straight-to-straight http://soundcloud.com/martin_lind/texture-vi http://soundcloud.com/martin_lind/suburbia-i-3]

sparks = []

content_types.each do |c|
  spark_types.each do |s|
    spark = Spark.new(:content_type => c, :spark_type => s)
    
    case c
    when "L"
      spark.content = Faker::Internet.url
    when "C"
      spark.content = "User.create!(:name => #{Faker::Internet.user_name})"
    when "T"
      spark.content = Faker::Lorem.sentences.join(" ")
    when "P"
      spark.content = pictures.pop
    when "A"
      spark.content = audios.pop
    when "V"
      spark.content = videos.pop
    end
    
    spark.save
    
    (rand(4) + 1).times do
      t = tags.sample
      
      unless(spark.tags.include?(t))
        spark.tags << t
      end
    end
    
    (rand(3) + 1).times do
      u = users.sample
      
      unless(spark.users.include?(u))
        spark.users << u
      end
    end
    
    (rand(4)).times do
      comment = Comment.new(:comment_text => Faker::Lorem.sentences.join(" "))
      comment.user = users.sample
      comment.commentable = spark
      
      comment.save
    end
    
    sparks << spark
  end
end

15.times do
  idea = Idea.new(:description => Faker::Lorem.sentence)
  idea.user = users.sample
  
  idea.save
  
  (rand(5) + 1).times do
    s = sparks.sample
    
    unless(idea.sparks.include?(s))
      idea.sparks << s
    end
  end
  
  (rand(4) + 1).times do
    t = tags.sample
    
    unless(idea.tags.include?(t))
      idea.tags << t
    end
  end
  
  (rand(4)).times do
    comment = Comment.new(:comment_text => Faker::Lorem.sentences.join(" "))
    comment.user = users.sample
    comment.commentable = idea
    
    comment.save
  end
end
