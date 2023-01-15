package cs1501_p2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AutoCompleter implements AutoComplete_Inter {

    public DLB dictionary;
    public UserHistory uh;

    public AutoCompleter(String eng_dict_fname) throws FileNotFoundException {

        dictionary = new DLB();
        uh = new UserHistory();

        Scanner dictScanner = new Scanner(new File(eng_dict_fname));
        while(dictScanner.hasNext()){
            dictionary.add(dictScanner.nextLine());
        }
        dictScanner.close();
    }

    public AutoCompleter(String eng_dict_fname, String uhist_state_fname) throws FileNotFoundException {

        dictionary = new DLB();
        uh = new UserHistory();

        Scanner dictScanner = new Scanner(new File(eng_dict_fname));
        while(dictScanner.hasNext()){
            dictionary.add(dictScanner.nextLine());
        }
        dictScanner.close();

        Scanner uhScanner = new Scanner(new File(uhist_state_fname));
        while(uhScanner.hasNext()){
            uh.add(uhScanner.nextLine());
        }
        uhScanner.close();
    }

    @Override
    public ArrayList<String> nextChar(char next) {
        ArrayList<String> suggestions = new ArrayList<>();
        if(uh.root != null){
            uh.searchByChar(next);
            suggestions = uh.suggest();
            if (suggestions.size() < 5){
                ArrayList<String> tempSugg = new ArrayList<>();
                dictionary.searchByChar(next);
                tempSugg = dictionary.suggest();
                for (String curr: tempSugg){
                    if (!suggestions.contains(curr)){
                        suggestions.add(curr);
                    }
                    if (suggestions.size() == 5) break;
                }
            }
        } else if (dictionary != null){
            dictionary.searchByChar(next);
            suggestions = dictionary.suggest();
        }
        return suggestions;
    }

    @Override
    public void finishWord(String cur) {
        dictionary.resetByChar();
        uh.resetByChar();
        dictionary.add(cur);
        uh.add(cur);      
    }

    @Override
    public void saveUserHistory(String fname) {
        if (fname.equals(null)) return;
        File output = new File(fname);
        FileWriter writer;
        try {
            writer = new FileWriter(output);
            for(String word: uh.traverse()){
                for(int i = 0; i < uh.repetitions.get(word); i++){
                    try {
                        writer.append(word).append("\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } 
            writer.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
    }
    
}
