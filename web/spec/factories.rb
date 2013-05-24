FactoryGirl.define do
  
  factory :user do |a|
    a.name      "max"
    a.email     "max@luzuriaga.com"
    a.password  "foobar"
  end
  
  factory :spark do |s|
    s.spark_type    "I"
    s.content_type  "T"
    s.content       "Kittens"
  end
  
  factory :idea do |p|
    p.description "Kittens, yo"
  end
  
  factory :tag do |c|
    c.tag_text  "stuff"
  end
  
  factory :comment do |c|
    c.comment_text  "Proin quis tortor orci. Etiam at risus et justo."
  end
  
  sequence :email do |n|
    "user-#{n}@example.com"
  end
  
  sequence :username do |n|
    "user-#{n}"
  end
  
  sequence :content do |n|
    "some-content-#{n}"
  end
  
end
