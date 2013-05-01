require 'test_helper'

class SparksControllerTest < ActionController::TestCase
  setup do
    @spark = sparks(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:sparks)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create spark" do
    assert_difference('Spark.count') do
      post :create, :spark => { :content => @spark.content, :content_hash => @spark.content_hash, :content_type => @spark.content_type, :spark_type => @spark.spark_type }
    end

    assert_redirected_to spark_path(assigns(:spark))
  end

  test "should show spark" do
    get :show, :id => @spark
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @spark
    assert_response :success
  end

  test "should update spark" do
    put :update, :id => @spark, :spark => { :content => @spark.content, :content_hash => @spark.content_hash, :content_type => @spark.content_type, :spark_type => @spark.spark_type }
    assert_redirected_to spark_path(assigns(:spark))
  end

  test "should destroy spark" do
    assert_difference('Spark.count', -1) do
      delete :destroy, :id => @spark
    end

    assert_redirected_to sparks_path
  end
end
