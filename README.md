# Simple Subreddits Recommender System
[Private]

Simple Big Data project to build a recommender system that recommends subreddits to users based on their participation.

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
