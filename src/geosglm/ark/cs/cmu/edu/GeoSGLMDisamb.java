package geosglm.ark.cs.cmu.edu;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.io.input.BoundedInputStream;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import com.cedarsoftware.util.io.JsonReader;
import com.cedarsoftware.util.io.JsonWriter;
 

/**
 * Code for learning geographically-informed word embeddings, as used in Bamman
 * et al. 2014,
 * "Distributed Representations of Geographically Situated Language" (ACL). This
 * draws on code from Mikolov et al. 2013,
 * "Efficient estimation of word representations in vector space" (ICLR).
 * https://code.google.com/p/word2vec/ (Apache 2.0)
 * 
 */
public class GeoSGLMDisamb {
    HashMap<String, HashMap<String , Double>> vocabProb;

    public GeoSGLMDisamb() {
        vocabProb = Maps.newHashMap();
    }
   
    // Return log probability by just taking the log.
    public double score_word_pair(String w, String c, GeoSGLMNoNormalize model_n, String domain) {
        double score = model_n.score_word_pair(w, c, domain);
        double prior = vocabProb.get(domain).get(c);
        System.out.println(String.format("%s %s %s %.6f %.6f %.6f", w, c, domain, score, prior, score*prior));
        return Math.log10(score * prior);
    } 

	public void setVocab(String vocabFile, String domain) {
	    HashMap<String, Double> vocabCounts;
		vocabCounts = Maps.newHashMap();
		try {
			BufferedReader in1 = new BufferedReader(new InputStreamReader(
					new FileInputStream(vocabFile), "UTF-8"));
			String str1;
			int c = 0;
			// Read valid vocab from file
			while ((str1 = in1.readLine()) != null) {
				try {
					String[] parts = str1.trim().split("\t");
					String word = parts[1];
                    Double word_freq = Double.parseDouble(parts[0]);
					vocabCounts.put(word, word_freq);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			in1.close();
        } catch (Exception e){
             e.printStackTrace();
        }   
        Double sum = 0.0;
        for (HashMap.Entry<String, Double> entry : vocabCounts.entrySet()) {
                sum += entry.getValue();
        }

        for (HashMap.Entry<String, Double> entry : vocabCounts.entrySet()) {
            entry.setValue(entry.getValue()/sum);
        }
        vocabProb.put(domain, vocabCounts);
    }     

	public static void main(String[] args) {
    	// Read JSON From InputStream
    	InputStream inStream = null;
        try {
      		inStream = new FileInputStream(args[0]);
      		JsonReader reader = new JsonReader(inStream);
      		GeoSGLMNoNormalize model_n = (GeoSGLMNoNormalize) reader.readObject();
            GeoSGLMDisamb disamb = new GeoSGLMDisamb();
            String location1 = args[1];
            String vocab_one = args[2];
            String location2 = args[3];
            String vocab_two = args[4];
            disamb.setVocab(vocab_one, location1);
            disamb.setVocab(vocab_two, location2);
            System.out.println(String.format("%s:%.6f", location1, disamb.vocabProb.get(location1).get("the")));
            Scanner input = new Scanner(System.in);
            System.out.println("Enter a word:");
            while (input.hasNext()) {
                String str = input.nextLine();
                String[] tokens = str.split(" ");
                String w = tokens[0];
                String[] context_words = Arrays.copyOfRange(tokens, 1, tokens.length); 
                double tploc1 = Math.log10(1.0);
                double tploc2 = Math.log10(1.0);
                for (String cw: context_words) {
                    double sloc1 = disamb.score_word_pair(w, cw, model_n, location1);
                    double sloc2 = disamb.score_word_pair(w, cw, model_n, location2);
                    tploc1 = tploc1 + sloc1;
                    tploc2 = tploc2 + sloc2;
                    System.out.println(String.format("%s:%s %s=%.6f %s=%.6f",w, cw, location1, sloc1, location2, sloc2));
                }
               System.out.println(String.format("Final %s: %s=%.6f %s=%.6f",w, location1, tploc1, location2, tploc2));
            }
    	} catch (FileNotFoundException e) {
     		 e.printStackTrace();
    	}
    }
}
