"""
Load up the learned word embeddings in sys.argv[1] in memory; for a given search term q, find the 10 closest terms to q in each of the 51 states.

"""

import sys,math,operator

import numpy as np
from numpy import linalg as LA


def get_embeddings(word, embeddings):
	word_emb = {}
	for n in sorted(embeddings);
		if word in embeddings[n]:
			word_emb[n] = embeddings[n][word]
	return word_emb


# normalize vectors for faster cosine similarity calculation
def normalize(embeddings):
	for name in embeddings:
		for word in embeddings[name]:
			a=embeddings[name][word]
			norm=LA.norm(a, 2)

			a /= norm	
			embeddings[name][word]=a

# get all active facets in embeddings
def getFacets(filename):
	file=open(filename)
	facets={}
	for i, line in enumerate(file):
                if i == 0:
                   continue
		cols=line.rstrip().split(" ")
		facets[cols[0]]=1
	file.close()

	# don't count the base facet
	del facets["MAIN"]

	return facets.keys()

def process(filename):
	file=open(filename)

	embeddings={}

	facets=getFacets(filename)

	# if you want to only consider a few metadata facets and not all 51 states, do that here.  e.g.:
	# facets=["MA", "PA"]
	facets = facets + ['MAIN']
	for facet in facets:
		embeddings[facet]={}

	for line in file:
		cols=line.rstrip().split(" ")
		if len(cols) < 10:
			continue

		facet=cols[0]

		if facet != "MAIN" and facet not in embeddings:
			continue

		word=cols[1]
		vals=cols[2:]
		a=np.array(vals, dtype=float);
		size=len(vals)

		## 
		# State embeddings for a word = the MAIN embedding for that word *plus* the state-specific deviation
		# e.g.
		# "wicked" in MA = wicked/MAIN + wicked/MA
		##

		if facet == "MAIN":
			for n in embeddings:
				if word not in embeddings[n]:
					embeddings[n][word]=np.zeros(size)
				
				embeddings[n][word]+=a

		else:
			if word not in embeddings[facet]:
				embeddings[facet][word]=np.zeros(size)
				
			embeddings[facet][word]+=a
		
	file.close()

	normalize(embeddings)
	return embeddings
