import org.apache.spark.mllib.recommendation.ALS
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel
import org.apache.spark.mllib.recommendation.Rating

val data = sc.textFile("../data-files/dataset-rating-format.txt")
val ratings = data.map(line => line.split(",")).map(tokens => Rating(tokens(0).toInt, tokens(1).toInt, tokens(2).toDouble))

val ratingsSplitArray = ratings.randomSplit(Array(0.8, 0.2))
val trainingData = ratingsSplitArray(0)
val validationData = ratingsSplitArray(1)

val validationForPredictionData = validationData.map(rating => (rating.user, rating.product))

// Build the recommendation model using ALS
// val ranks = List(4, 5, 6, 7, 8, 9, 10)
// val numIterations = 10

// var rank = 0
// var bestRank = -1
// var minError = Double.MaxValue
// var tolerance = 10
// var model: MatrixFactorizationModel = null

// for(rank <- ranks) {
// 	model = ALS.trainImplicit(trainingData, rank, numIterations)

// 	// Evaluate the model on rating data
// 	val predictions = model.predict(validationForPredictionData).map(rating => ((rating.user, rating.product), rating.rating))
// 	val ratesAndPreds = validationData.map(rating => ((rating.user, rating.product), rating.rating)).join(predictions)
// 	val RMSE = scala.math.sqrt(ratesAndPreds.map(el => scala.math.pow(el._2._2 - el._2._1, 2)).mean())
// 	val accuracy = (ratesAndPreds.filter(el => scala.math.abs(el._2._2 - el._2._1) < tolerance).count().toDouble / ratesAndPreds.count()) * 100
// 	println("For rank " + rank + " the RMSE is " + RMSE + " and accuracy is " + accuracy + "%")

// 	if(RMSE < minError) {
// 		minError = RMSE
// 		bestRank = rank
// 	}
// }
// 
// println("The best model was trained with rank " + bestRank)

// *********
// By running the code above, we determined that the best rank for this dataset was 9. 
// So now, we simply train the model with that rank and save it.
// *********

val model = ALS.trainImplicit(trainingData, 9, 10);
model.save(sc, "models/implicitRank9")

System.exit(0)
