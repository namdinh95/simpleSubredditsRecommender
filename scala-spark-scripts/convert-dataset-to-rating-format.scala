import org.apache.spark.SparkContext._

var userLookupRDD = sc.textFile("user-lookup.txt").map(line => line.split(",")).map(tokens => (tokens(0), tokens(1).toInt))
var subredditLookupRDD = sc.textFile("subreddit-lookup.txt").map(line => line.split(",")).map(tokens => (tokens(0), tokens(1).toInt))
var dataset = sc.textFile("final.txt").map(line => line.split("\t")).map(tokens => (tokens(0), tokens(1), tokens(2).toDouble))

val userMapB = sc.broadcast(userLookupRDD.collectAsMap())
val subredditMapB = sc.broadcast(subredditLookupRDD.collectAsMap())

var processed_dataset = dataset.map(element => (userMapB.value(element._1), subredditMapB.value(element._2), element._3))

// check processed_dataset (uncomment if needed)
// val revUserMapB = sc.broadcast(userLookupRDD.map(element => (element._2, element._1)).collectAsMap())
// val revSubredditMapB = sc.broadcast(subredditLookupRDD.map(element => (element._2, element._1)).collectAsMap())
// val datasetMapB = sc.broadcast(dataset.map(element => (element._1 + "," + element._2, element._3)).collectAsMap())

// var conflictedEntriesRDD = processed_dataset.filter(pelement => pelement._3 != datasetMapB.value(revUserMapB.value(pelement._1) + "," + revSubredditMapB.value(pelement._2)))
// println("If the int on the next line is 0, we're all good!")
// conflictedEntriesRDD.count

processed_dataset.coalesce(1).saveAsTextFile("dataset-rating-format")

System.exit(0)