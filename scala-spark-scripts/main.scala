import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.mllib.recommendation.Rating

val training_RDD = sc.textFile("training-data.txt").map(line => line.split("\t")).map(tokens => (tokens(0), tokens(1), tokens(2))).cache();

// training_RDD.take(10);

val validation_RDD = sc.textFile("validation-data.txt").map(line => line.split("\t")).map(tokens => (tokens(0), tokens(1), tokens(2))).cache();

// validation_RDD.take(10);

val test_RDD = sc.textFile("test-data.txt").map(line => line.split("\t")).map(tokens => (tokens(0), tokens(1), tokens(2))).cache();

// test_RDD.take(10);

// Build the recommendation model using ALS
val rank = 10
val numIterations = 10
val model = ALS.train(training_RDD, rank, numIterations, 0.01)

// var iterations = 10
// var regularization_parameter = 0.1
// var ranks = List(4, 8, 12)
// var errors = List(0, 0, 0)
// var err = 0
// var tolerance = 0.02

// var min_error = Float.MaxValue
// var best_rank = -1
// var best_iteration = -1

// // initialize to dummy value
// var rank = 5

// for(rank <- ranks) {
//     var model = ALS.train(training_RDD, rank, iterations, regularization_parameter)
//     var predictions = model.predict(validation_for_predict_RDD).map(r => ((r._1, r._2, r._3)))
//     var rates_and_preds = validation_RDD.map(r => (r._1.toInt(), r._2.toInt(), r._3.parseDouble())).join(predictions)
//     var error = math.sqrt(rates_and_preds.map(r => (math.pow(r(1)(0) - r(1)(1), 2)).mean())

//     errors(err) = error
//     err += 1
//     println(s"For rank $rank the RMSE is $error")
//     if (error < min_error) {
//         min_error = error
//         best_rank = rank
//     }
// }

System.exit(0)