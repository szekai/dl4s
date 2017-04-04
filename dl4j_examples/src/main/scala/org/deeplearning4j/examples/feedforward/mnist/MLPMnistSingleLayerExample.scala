package org.deeplearning4j.examples.feedforward.mnist

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator
import org.deeplearning4j.eval.Evaluation
import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.layers.{DenseLayer, OutputLayer}
import org.deeplearning4j.nn.conf.{NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction
import org.slf4j.LoggerFactory

/** A Simple Multi Layered Perceptron (MLP) applied to digit classification for
  * the MNIST Dataset (http://yann.lecun.com/exdb/mnist/).
  *
  * This file builds one input layer and one hidden layer.
  *
  * The input layer has input dimension of numRows*numColumns where these variables indicate the
  * number of vertical and horizontal pixels in the image. This layer uses a rectified linear unit
  * (relu) activation function. The weights for this layer are initialized by using Xavier initialization
  * (https://prateekvjoshi.com/2016/03/29/understanding-xavier-initialization-in-deep-neural-networks/)
  * to avoid having a steep learning curve. This layer will have 1000 output signals to the hidden layer.
  *
  * The hidden layer has input dimensions of 1000. These are fed from the input layer. The weights
  * for this layer is also initialized using Xavier initialization. The activation function for this
  * layer is a softmax, which normalizes all the 10 outputs such that the normalized sums
  * add up to 1. The highest of these normalized values is picked as the predicted class.
  *
  */
object MLPMnistSingleLayerExample {
  val log = LoggerFactory.getLogger(MLPMnistSingleLayerExample.getClass())

  def main(args: Array[String]): Unit = {
    //number of rows and columns in the input pictures
    val numRows = 28
    val numColumns = 28
    val outputNum = 10 // number of output classes
    val batchSize = 128 // batch size for each epoch
    val rngSeed = 123 // random number seed for reproducibility
    val numEpochs = 15 // number of epochs to perform

    //Get the DataSetIterators:
    val mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed)
    val mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed)

    log.info("Build model....")
    val conf = new NeuralNetConfiguration.Builder()
      .seed(rngSeed) //include a random seed for reproducibility
      // use stochastic gradient descent as an optimization algorithm
      .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
      .iterations(1)
      .learningRate(0.006) //specify the learning rate
      .updater(Updater.NESTEROVS).momentum(0.9) //specify the rate of change of the learning rate.
      .regularization(true).l2(1e-4)
      .list()
      .layer(0, new DenseLayer.Builder() //create the first, input layer with xavier initialization
        .nIn(numRows * numColumns)
        .nOut(1000)
        .activation(Activation.RELU)
        .weightInit(WeightInit.XAVIER)
        .build())
      .layer(1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create hidden layer
        .nIn(1000)
        .nOut(outputNum)
        .activation(Activation.SOFTMAX)
        .weightInit(WeightInit.XAVIER)
        .build())
      .pretrain(false).backprop(true) //use backpropagation to adjust weights
      .build()

    val model = new MultiLayerNetwork(conf)
    model.init()
    //print the score with every 1 iteration
    model.setListeners(new ScoreIterationListener(1))

    log.info("Train model....")
    for(i <- 0 until numEpochs ){
      model.fit(mnistTrain)
    }

    log.info("Evaluate model....")
    val eval = new Evaluation(outputNum) //create an evaluation object with 10 possible classes

    while(mnistTest.hasNext()){
      val next = mnistTest.next()
      val output = model.output(next.getFeatureMatrix()) //get the networks prediction
      eval.eval(next.getLabels(), output) //check the prediction against the true class
    }

    log.info(eval.stats())
    log.info("****************Example finished********************")
  }
}
