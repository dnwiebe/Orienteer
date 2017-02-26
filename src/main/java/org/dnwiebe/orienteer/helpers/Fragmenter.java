package org.dnwiebe.orienteer.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dnwiebe on 2/23/17.
 */
public class Fragmenter {

  public List<String> fragment(String name) {
    FragmentationState state = new FragmentationState();
    for (int i = 0; i < name.length(); i++) {
      state.acceptCharacter(name.charAt(i));
    }
    state.finish();
    return state.getResult();
  }

  static private class FragmentationState {
    private int accumulatedUppers = 1;
    private int accumulatedDigits = 0;
    private StringBuilder wordSoFar = new StringBuilder();
    private List<String> result = new ArrayList<String>();

    public void acceptCharacter(char c) {
      if (Character.isUpperCase(c)) {
        processUpperCaseCharacter(c);
      } else if (Character.isDigit(c)) {
        processDigit(c);
      } else {
        processLowerCaseCharacter(c);
      }
    }

    public void finish() {
      result.add(wordSoFar.toString());
    }

    public List<String> getResult() {
      return result;
    }

    private void processDigit (char c) {
      if ((accumulatedDigits == 0) && (wordSoFar.length() > 0)) {
        finishWord();
      }
      wordSoFar.append(c);
      accumulatedDigits++;
      accumulatedUppers = 0;
    }

    private void processLowerCaseCharacter(char c) {
      finishUppercaseRegionIfNecessary();
      finishDigitRegionIfNecessary();
      wordSoFar.append(c);
      accumulatedUppers = 0;
    }

    private void processUpperCaseCharacter(char c) {
      finishLowercaseRegionIfNecessary();
      finishDigitRegionIfNecessary();
      wordSoFar.append(c);
      accumulatedUppers++;
    }

    private void finishWord () {
      result.add(wordSoFar.toString());
      wordSoFar = new StringBuilder();
    }

    private char reduceByOneChar(StringBuilder buf) {
      char lastChar = buf.charAt(buf.length() - 1);
      buf.setLength(buf.length() - 1);
      return lastChar;
    }

    private void finishUppercaseRegionIfNecessary () {
      if (accumulatedUppers > 1) {
        char firstOfThisWord = reduceByOneChar(wordSoFar);
        finishWord ();
        wordSoFar.append(firstOfThisWord);
      }
    }

    private void finishLowercaseRegionIfNecessary() {
      if ((accumulatedUppers == 0) && (accumulatedDigits == 0)) {
        finishWord ();
      }
    }

    private void finishDigitRegionIfNecessary() {
      if (accumulatedDigits > 0) {
        finishWord ();
        accumulatedDigits = 0;
      }
    }
  }
}
