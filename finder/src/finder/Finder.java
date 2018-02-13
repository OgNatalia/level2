package finder;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Finder {
    
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        if(args.length==0){
            System.out.println("You don't pass in a parameter of the program!");
        } else {            
            System.out.println("Lead the line:");
            Scanner in = new Scanner(System.in);
            while(true){                

                String line = in.nextLine();  
                if(!"".equals(line)){                
                    list.add(line);                    
                } else {
                    break;
                }              
            }   
            String patSt = "";
            for(int i=0; i<args.length; i++){
                patSt += "|" + args[i].trim();   
            } 
            
            System.out.println("Output: ");
            try{
                Pattern p = Pattern.compile(patSt);                           
                for(String l:list){
                    String[] words = l.split(" ");
                    for(int j=0; j<words.length; j++){
                        Matcher m = p.matcher(words[j]);
                        if(m.matches()==true){
                            System.out.println(l);
                            break;
                        }
                    }   
                }
            }catch(PatternSyntaxException e){
                for(int i=0; i<args.length; i++){
                    String param = args[i];
                    //System.out.println(param);
                    for(String l:list){
                        String[] words = l.split(" ");
                        for(int j=0; j<words.length; j++){
                            if (words[j].equals(param)){
                                System.out.println(l);
                                break;
                            }
                        }                    
                    }
                }  
            }   
        }        
    }   
}
