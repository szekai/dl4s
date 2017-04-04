

object Common {
  def text = "org.example"
//  Change the nd4j.backend property to nd4j-cuda-7.5-platform or nd4j-cuda-8.0-platform to use CUDA GPUs
  def nd4j_backend = "nd4j-native-platform"
  def nd4j_version = "0.8.0"
  def dl4j_version = "0.8.0"
  def datavec_version = "0.8.0"
  def arbiter_version = "0.8.0"
  def rl4j_version = "0.8.0"
//  For Spark examples: change the _1 to _2 to switch between Spark 1 and Spark 2
  def dl4j_spark_version = "0.8.0_spark_1"
  def datavec_spark_version = "0.8.0_spark_1"

//  Scala binary version: DL4J's Spark and UI functionality are released with both Scala 2.10 and 2.11 support
  def scala_binary_version = "2.12.1"

  def guava_version = "19.0"
  def logback_version = "1.1.7"
  def jfreechart_version = "1.0.13"
  def jcommon_version = "1.0.23"
}