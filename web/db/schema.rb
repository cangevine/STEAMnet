# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20130501134631) do

  create_table "ideas", :force => true do |t|
    t.text     "description"
    t.datetime "created_at",  :null => false
    t.datetime "updated_at",  :null => false
  end

  create_table "ideas_sparks", :id => false, :force => true do |t|
    t.integer "idea_id"
    t.integer "spark_id"
  end

  create_table "ideas_users", :id => false, :force => true do |t|
    t.integer "idea_id"
    t.integer "user_id"
  end

  create_table "sparks", :force => true do |t|
    t.string   "spark_type"
    t.string   "content_type"
    t.text     "content"
    t.text     "content_hash"
    t.datetime "created_at",   :null => false
    t.datetime "updated_at",   :null => false
  end

  create_table "sparks_users", :id => false, :force => true do |t|
    t.integer "spark_id"
    t.integer "user_id"
  end

  create_table "users", :force => true do |t|
    t.string   "name"
    t.text     "password_hash"
    t.string   "email"
    t.datetime "created_at",    :null => false
    t.datetime "updated_at",    :null => false
  end

end
