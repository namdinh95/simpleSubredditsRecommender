var dataset = sc.textFile("final.txt").map(line => line.split("\t")).map(tokens => (tokens(0), tokens(1), tokens(2)))

var idCounter = 1
var userMap = collection.mutable.Map[String, Int]()

// go through each element of the dataset and add mapping to userMap if not already present
// have to run spark-shell with 4 gig memory to driver, and 2 CPU cores to driver to be able to complete this

dataset.collect().foreach(
		x =>
		if(!userMap.contains(x._1)) {
			userMap += (x._1 -> idCounter)
			idCounter += 1
		}
	)

// write usermap to a single text file
var userLookupRDD = sc.parallelize(userMap.toSeq)
userLookupRDD.coalesce(1).saveAsTextFile("user-lookup")

System.exit(0)