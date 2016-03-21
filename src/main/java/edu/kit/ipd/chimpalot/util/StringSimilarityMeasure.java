package edu.kit.ipd.chimpalot.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
public class StringSimilarityMeasure {
	private int n = 2;

	public Set<List<String>> getNGrams(List<String> stringList) {
		Set<List<String>> ngrams = new HashSet<List<String>>();

		for (int i = 0; i < stringList.size() - (n - 1); i++) {
			// Generate n-gram at index i
			List<String> ngram = new ArrayList<String>();
			for (int k = 0; k < n; k++) {
				String token = stringList.get(i + k);
				token = token.toLowerCase();
				ngram.add(token);
			}

			// Add
			ngrams.add(ngram);
		}

		return ngrams;
	}
	public double getNormalizedSimilarity(Set<List<String>> suspiciousNGrams,
            Set<List<String>> originalNGrams)
    {
        // Compare using the Jaccard similarity coefficient (Manning & Schütze, 1999)
        Set<List<String>> commonNGrams = new HashSet<List<String>>();
        commonNGrams.addAll(suspiciousNGrams);
        commonNGrams.retainAll(originalNGrams);

        Set<List<String>> unionNGrams = new HashSet<List<String>>();
        unionNGrams.addAll(suspiciousNGrams);
        unionNGrams.addAll(originalNGrams);

        double norm = unionNGrams.size();
        double sim = 0.0;

        if (norm > 0.0) {
            sim = commonNGrams.size() / norm;
        }

        return sim;
    }


}
