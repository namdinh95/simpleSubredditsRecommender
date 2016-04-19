# Order JSON's keys alphabetically.
# Accept 1 argument as file's name.

import json, sys

inFile = open(sys.argv[1])
outFile = open('testOutput.json', 'a')

for line in inFile:
    r = json.loads(line)
    json.dump(r, outFile, sort_keys=True)
    outFile.write('\n')

