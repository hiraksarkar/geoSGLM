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
	public static void main(String[] args) {
    	// Read JSON From InputStream
    	InputStream inStream = null;
        try {
      		inStream = new FileInputStream("test.json");
      		JsonReader reader = new JsonReader(inStream);
      		GeoSGLMNoNormalize model_n = (GeoSGLMNoNormalize) reader.readObject();
            Scanner input = new Scanner(System.in);
            System.out.println("Enter a word:");
            while (input.hasNext()) {
                String str = input.nextLine();
                String[] tokens = str.split(" ");
                double sports = model_n.score_word_pair(tokens[0], tokens[1], "sports");
                double finance = model_n.score_word_pair(tokens[0], tokens[1], "business");
                double wikipedia = model_n.score_word_pair(tokens[0], tokens[1], "wiki");
                System.out.println(String.format("sports=%.6f finance=%.6f, wikipedia=%.6f",sports, finance, wikipedia));
            }
    	} catch (FileNotFoundException e) {
     		 e.printStackTrace();
    	}
    }
}
