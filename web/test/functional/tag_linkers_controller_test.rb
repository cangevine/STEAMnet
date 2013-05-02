require 'test_helper'

class TagLinkersControllerTest < ActionController::TestCase
  setup do
    @tag_linker = tag_linkers(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:tag_linkers)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create tag_linker" do
    assert_difference('TagLinker.count') do
      post :create, :tag_linker => {  }
    end

    assert_redirected_to tag_linker_path(assigns(:tag_linker))
  end

  test "should show tag_linker" do
    get :show, :id => @tag_linker
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @tag_linker
    assert_response :success
  end

  test "should update tag_linker" do
    put :update, :id => @tag_linker, :tag_linker => {  }
    assert_redirected_to tag_linker_path(assigns(:tag_linker))
  end

  test "should destroy tag_linker" do
    assert_difference('TagLinker.count', -1) do
      delete :destroy, :id => @tag_linker
    end

    assert_redirected_to tag_linkers_path
  end
end
