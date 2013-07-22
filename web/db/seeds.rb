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

pictures = %w[image1.jpg image2.jpg image3.jpg]
videos = %w[http://www.youtube.com/watch?v=FSi2fJALDyQ http://www.youtube.com/watch?v=DF8nHmHUCAs http://www.youtube.com/watch?v=iCE1W-aGsWU]
video_thumbs = %w[video1.jpeg video2.jpeg video3.jpeg]
audios = %w[audio1.mp4 audio2.m4a audio3.m4a]
links = %w[http://developer.android.com/reference/java/io/FileOutputStream.html http://www.reddit.com http://rapgenius.com/Kanye-west-hold-my-liquor-lyrics]
link_thumbs = %w[link1.jpeg link2.jpeg link3.jpeg]
code = %w[https://gist.github.com/aphillips915/6045025 https://gist.github.com/aphillips915/6051260 https://gist.github.com/aphillips915/6051262]
code_thumbs = %w[code1.jpeg code1.jpeg code3.jpeg]

sparks = []

content_types.each do |c|
  spark_types.each do |s|
    spark = Spark.new(:content_type => c, :spark_type => s)
    
    case c
    when "L"
      spark.content = links.pop
      spark.file = File.new("db/seed_files/#{link_thumbs.pop}", "r")
    when "C"
      spark.content = code.pop
      spark.file = File.new("db/seed_files/#{code_thumbs.pop}", "r")
    when "T"
      spark.content = Faker::Lorem.sentences.join(" ")
    when "P"
      filename = pictures.pop
      spark.content = filename
      spark.file = File.new("db/seed_files/#{filename}", "r")
    when "A"
      filename = audios.pop
      spark.content = filename
      spark.file = File.new("db/seed_files/#{filename}", "r")
    when "V"
      spark.content = videos.pop
      spark.file = File.new("db/seed_files/#{video_thumbs.pop}", "r")
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
