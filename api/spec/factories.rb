FactoryGirl.define do
  
  factory :user do
    sequence :name do |n|
      "user-#{n}"
    end
    sequence :email do |n|
      "user-#{n}@example.com"
    end
  end
  
  factory :spark do
    spark_type "I"
    content_type "P"
    file Rack::Test::UploadedFile.new('spec/fixtures/images/test.jpg', 'image/jpeg')
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
  
  factory :authentication do
    provider "developer"
    sequence :uid do |n|
      "id#{n}"
    end
  end
  
  factory :device do
    sequence :registration_id do |n|
      "id#{n}"
    end
  end
  
end
