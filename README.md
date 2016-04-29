# Simple Subreddits Recommender System

A Big Data project to build a recommender system that recommends subreddits to users based on their participation.

The Reddit dataset can be found here: https://goo.gl/GHckIi

### Pre-process the data

Run the Python script first to re-order the unorganized keys in the JSON file so that Pig can properly filter the data.

### Running Scala Spark scripts

We're taking the "easy way out" with the spark scripts, using `collect()` and `coalesce()`. This obviously puts pressure on the driver program, so ideally give 4 gigs of memory and 2 cores to the driver:

```bash
spark-shell --driver-memory 4G --driver-cores 2 -i <script-name>
```

Also, when Spark executes `saveAsTextFile()` on an RDD, the text file has parentheses as the first and last character on each line. To get rid of them:

```bash
sed 's/^.//' <input-file> > temp.txt
sed 's/.$//' temp.txt > <final-output-file>
rm temp.txt
```
