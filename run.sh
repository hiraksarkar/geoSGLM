# input file
DATA=data/corpus_nl.txt

# vocab file
VOCABFILE=data/vocab.txt

# set of metadata facets
FEATUREFILE=data/years.txt

# output file to write embeddings to
OUTFILE=data/out.embeddings

# Output file to write JSON of output
JSON_OUTFILE=data/out.json

# max vocab size
MAXVOCAB=100000

# dimensionality of embeddings
DIMENSIONALITY=200

# L2 regularization parameter
L2=0.0001

./runjava geosglm.ark.cs.cmu.edu/GeoSGLMNormalize $DATA $VOCABFILE $FEATUREFILE $OUTFILE $MAXVOCAB $DIMENSIONALITY $L2 0 $JSON_OUTFILE
