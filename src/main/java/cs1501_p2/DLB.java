package cs1501_p2;

import java.util.ArrayList;

public class DLB implements Dict{

    public DLBNode root;
    public DLBNode charSearch;
    public String word;


    public DLB (){
        charSearch = null;
        word = "";
    }


    @Override
    public void add(String key){

        if (key == null) throw new IllegalArgumentException("Invalid Key");
        if (key.equals("")) throw new IllegalArgumentException("Invalid Key");

        DLBNode curr = root;

        if (root == null){
            root = new DLBNode(key.charAt(0));
            curr = root;
            charSearch = root;
            for(int i = 1; i < key.length(); i++){
                curr.setDown(new DLBNode(key.charAt(i)));
                curr = curr.getDown();
            }
            curr.setDown(new DLBNode('^'));

        } else {

            for(int i = 0; i < key.length(); i++){
                while(curr.getRight() != null && curr.getLet() != key.charAt(i)) curr = curr.getRight();
                if(curr.getLet() == key.charAt(i) && curr.getDown() != null){
                    if (i != key.length() - 1)
                        curr = curr.getDown();
                }
                else{
                    if(curr.getLet() != key.charAt(i)){
                        curr.setRight(new DLBNode(key.charAt(i)));
                        curr = curr.getRight();
                        for (int j = i + 1; j < key.length(); j++){
                            curr.setDown(new DLBNode(key.charAt(j)));                            
                            curr = curr.getDown();
                        }
                        curr.setDown(new DLBNode('^'));
                        break;
                    } else if(curr.getDown() == null){
                        for (int j = i; j < key.length(); j++){
                            curr.setDown(new DLBNode(key.charAt(j)));                            
                            curr = curr.getDown();
                        }
                        curr.setDown(new DLBNode('^'));
                        break;
                    }
                }
            }
            if (curr.getDown().getLet() != '^'){
                DLBNode temp = new DLBNode('^');
                temp.setRight(curr.getDown());
                curr.setDown(temp);
            }
        }
    }


    @Override
    public boolean contains(String key){
        if (key != null){
            DLBNode curr = root;
            for (int i = 0; i < key.length(); i ++){
                if (curr == null) return false;
                while (curr != null){
                    if (curr.getLet() == key.charAt(i)){
                        curr = curr.getDown();
                        break;
                    } else curr = curr.getRight();
                }
            }
            if (curr.getLet() == '^') return true;
        }
        return false;
    }

    @Override
    public boolean containsPrefix(String pre){
        if (pre != null){
            DLBNode curr = root;
            for (int i = 0; i < pre.length(); i++){
                if (curr == null) return false;
                while (curr != null){
                    if (curr.getLet() == pre.charAt(i)){
                        curr = curr.getDown();
                        break;
                    } else curr = curr.getRight();
                }
            }
            return true;
        }
        return false;
    }


    @Override
    public int searchByChar(char next){
        while (charSearch != null){
            if (charSearch.getLet() == next){
                word = word + charSearch.getLet();
                charSearch = charSearch.getDown();
                if (charSearch.getLet() != '^') return 0;
                if (charSearch.getLet() == '^'){
                    charSearch = charSearch.getRight();
                    if (charSearch == null) return 1;
                    else return 2;
                }
            } else charSearch = charSearch.getRight();
        }
        return -1;
    }

    

    @Override
    public void resetByChar(){
        charSearch = root;
        word = "";
    }

    public int suggestCounter;

    @Override
    public ArrayList<String> suggest(){
        suggestCounter = 0;
        ArrayList<String> suggestions = new ArrayList<>();
        DLBNode curr = charSearch;
        wordSuggestion(curr, suggestions, word, word.length()-1);
        return suggestions;
    }

    public void wordSuggestion (DLBNode curr, ArrayList<String> suggestions, String wd, int pos){
        if(suggestCounter<5 && curr != null){
            if(curr.getLet() == '^'){
                suggestions.add(wd);
                suggestCounter++;
                wordSuggestion(curr.getRight(), suggestions, wd, pos);
                wordSuggestion(curr.getDown(), suggestions, wd, pos);
            }else{
                wordSuggestion(curr.getDown(), suggestions, wd + curr.getLet(), pos++);
                wordSuggestion(curr.getRight(), suggestions, wd, pos);
            }
        }
    }    

    @Override
    public ArrayList<String> traverse(){
        String wd = "";
        ArrayList<String> dict = new ArrayList<>();
        trav_rec(root, dict, wd);
        return dict;
    }

    public void trav_rec(DLBNode curr, ArrayList<String> dict, String wd){
        if (curr != null){
            if (curr.getLet() == '^'){
                dict.add(wd);
            } 
            trav_rec(curr.getDown(), dict, wd + curr.getLet());
            trav_rec(curr.getRight(), dict, wd);
        }
    }

    @Override
    public int count(){
        return count_rec(root);
    }

    public int count_rec(DLBNode curr){
        if (curr == null) return 0;
        if (curr.getLet() == '^') return (count_rec(curr.getDown()) + count_rec(curr.getRight()) + 1);
        else return (count_rec(curr.getDown()) + count_rec(curr.getRight()));
    }


}
