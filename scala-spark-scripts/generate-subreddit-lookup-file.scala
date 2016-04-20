var dataset = sc.textFile("final.txt").map(line => line.split("\t")).map(tokens => (tokens(0), tokens(1), tokens(2)))

var idCounter = 1
var subredditMap = collection.mutable.Map[String, Int]()

// go through each element of the dataset and add mapping to subredditMap if not already present
// have to run spark-shell with 4 gig memory to driver, and 2 CPU cores to driver to be able to complete this

dataset.collect().foreach(
		x =>
		if(!subredditMap.contains(x._2)) {
			subredditMap += (x._2 -> idCounter)
			idCounter += 1
		}
	)

// write usermap to a single text file
var subredditLookupRDD = sc.parallelize(subredditMap.toSeq)
subredditLookupRDD.coalesce(1).saveAsTextFile("subreddit-lookup")

System.exit(0)