# encoding: UTF-8
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

ActiveRecord::Schema.define(:version => 20130531012743) do

  create_table "comments", :force => true do |t|
    t.text     "comment_text"
    t.integer  "user_id"
    t.datetime "created_at",       :null => false
    t.datetime "updated_at",       :null => false
    t.integer  "commentable_id"
    t.string   "commentable_type"
  end

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
    t.string   "content_hash"
    t.datetime "created_at",   :null => false
    t.datetime "updated_at",   :null => false
  end

  create_table "sparks_users", :id => false, :force => true do |t|
    t.integer "spark_id"
    t.integer "user_id"
  end

  create_table "tag_linkers", :force => true do |t|
    t.datetime "created_at",   :null => false
    t.datetime "updated_at",   :null => false
    t.integer  "tagable_id"
    t.string   "tagable_type"
    t.integer  "tag_id"
  end

  create_table "tags", :force => true do |t|
    t.text     "tag_text"
    t.datetime "created_at", :null => false
    t.datetime "updated_at", :null => false
  end

  create_table "users", :force => true do |t|
    t.string   "name"
    t.text     "password_digest"
    t.string   "email"
    t.datetime "created_at",      :null => false
    t.datetime "updated_at",      :null => false
  end

end
