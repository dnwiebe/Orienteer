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
    private StringBuilder wordSoFar = new StringBuilder();
    private List<String> result = new ArrayList<String>();

    public void acceptCharacter(char c) {
      if (Character.isUpperCase(c)) {
        processUpperCaseCharacter(c);
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

    private void processLowerCaseCharacter(char c) {
      if (accumulatedUppers > 1) {
        char firstOfThisWord = wordSoFar.charAt(wordSoFar.length() - 1);
        wordSoFar.setLength(wordSoFar.length() - 1);
        result.add(wordSoFar.toString());
        wordSoFar = new StringBuilder();
        wordSoFar.append(firstOfThisWord);
      }
      wordSoFar.append(c);
      accumulatedUppers = 0;
    }

    private void processUpperCaseCharacter(char c) {
      if (accumulatedUppers == 0) {
        result.add(wordSoFar.toString());
        wordSoFar = new StringBuilder();
      }
      wordSoFar.append(c);
      accumulatedUppers++;
    }
  }
}
