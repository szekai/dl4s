name := "dl4s"
import Common._
lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := scala_binary_version
)

lazy val baseDeps = Seq(
  "org.datavec" % "datavec-data-codec" % datavec_version,
  //ND4J backend. You need one in every DL4J project. Normally define artifactId as either "nd4j-native-platform" or "nd4j-cuda-7.5-platform"
  "org.nd4j" % nd4j_backend % nd4j_version,
  //Core DL4J functionality
  "org.deeplearning4j" % "deeplearning4j-core" % dl4j_version,
  //deeplearning4j-ui is used for HistogramIterationListener + visualization: see http://deeplearning4j.org/visualization
  "org.deeplearning4j" % "deeplearning4j-ui_2.11" % dl4j_version,
  //datavec-data-codec: used only in video example for loading video data
  "com.google.guava" % "guava" % guava_version,
  //Used in the feedforward/classification/MLP* and feedforward/regression/RegressionMathFunctions example
  "jfree" % "jfreechart" % jfreechart_version,
  "org.jfree" % "jcommon" % jcommon_version,
  //Used for downloading data in some of the examples
  "org.apache.httpcomponents" % "httpclient" % "4.3.5"
)
//lazy val arbiter_examples = project
lazy val dl4j_examples = (project in file("dl4j_examples")).settings(
  commonSettings,
  //Core DL4J functionality
  libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % dl4j_version,
  libraryDependencies ++= baseDeps
)
