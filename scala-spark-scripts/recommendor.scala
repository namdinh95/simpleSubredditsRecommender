import org.apache.spark.mllib.recommendation.Rating
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel

// Get username, convert to userID
val userName = readLine("Enter user name to recommend: ")
val userData = sc.textFile("../data-files/user-lookup.txt")
val userLookup = userData.map(line => line.split(",")).map(tokens => (tokens(0), tokens(1).toInt))
// Lookup might fail if no username existed
var userID = -1
try {
    userID = userLookup.lookup(userName)(0)
} catch {
    case e: ArrayIndexOutOfBoundsException => {
        println("No such username found")
        System.exit(-1)
    }
}
val id = sc.broadcast(userID)

// Load model
val model = MatrixFactorizationModel.load(sc, "models/implicitRank9")
val data = sc.textFile("../data-files/dataset-rating-format.txt")
val ratings = data.map(line => line.split(",")).map(tokens => Rating(tokens(0).toInt, tokens(1).toInt, tokens(2).toDouble))
val subData = sc.textFile("../data-files/subreddit-lookup.txt")
val allSubs = subData.map(line => line.split(",")).map(tokens => tokens(1).toInt)

// Filter out all subreddits where this user has commented
val userSubs = ratings.filter(rating => rating.user == id.value).map(rating => (rating.product))

// Get subreddits that user hasn't commented on and map with userID
val userUnratedSubs = allSubs.subtract(userSubs).map(sub => (id.value, sub))

// Predict and get rid of userID
val predictRating = model.predict(userUnratedSubs).map(rating => (rating.product, rating.rating))

// Get RDD that have subreddits and number of people commented in them
val subsTotalComments = ratings.map(rating => (rating.product, rating.user)).reduceByKey(_ + _)
// val subUsers = ratings.map(rating => (rating.product, rating.user)).groupByKey()
// val subsTotalComments = subUsers.map(sub => (sub._1, sub._2.count(user => user.isInstanceOf[Int])))

// Filter (join) so we are only left with subreddits where at least 20 people have commented
// Sort and take top 10 recommended subs
val predictRatingWithTotal = predictRating.join(subsTotalComments)
val topSubs = sc.parallelize(predictRatingWithTotal.filter(rating => rating._2._2 >= 20).top(10)(Ordering[Double].on(rating => rating._2._1)))

// Nice format with names
val allSubsWithName = subData.map(line => line.split(",")).map(tokens => (tokens(1).toInt, tokens(0)))
val topSubsWithName = topSubs.join(allSubsWithName)
val topSubsName = topSubsWithName.map(sub => (sub._1, sub._2._2))

println("The top 10 subreddits recommended for user " + userName + " are:")
topSubsName.foreach(sub => println(sub._2))

System.exit(0)