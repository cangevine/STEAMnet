json.spark_is_new true if @spark_is_new
json.partial! 'spark', spark: @spark, lite: params["lite"] == "true"