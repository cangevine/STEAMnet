FactoryGirl.define do
  
  factory :user do
    sequence :name do |n|
      "user-#{n}"
    end
    sequence :email do |n|
      "user-#{n}@example.com"
    end
    password "foobar"
  end
  
  factory :spark do
    spark_type "I"
    content_type "T"
    sequence :content do |n|
      "Some-content #{n}"
    end
  end
  
  factory :idea do
    sequence :description do |n|
      "Some description #{n}"
    end
  end
  
  factory :tag do
    sequence :tag_text do |n|
      "some-tag-#{n}"
    end
  end
  
  factory :comment do
    sequence :comment_text do |n|
      "Some comment #{n}"
    end
  end
  
end
