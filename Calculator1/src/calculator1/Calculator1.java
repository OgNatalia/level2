package calculator1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator1 {
   
    public static void main(String[] args){
        System.out.println("Ведите строки:");
        Scanner in = new Scanner(System.in);
        String line = in.nextLine(); 
        String lineRes; 
        Pattern pat = Pattern.compile(".+([a-zA-Zа-яА-Я])+.|[^ \\^\\d)(.,*/+-]");
        Pattern pat1 = Pattern.compile("\\d +\\d");
        Matcher mat = pat.matcher(line);
        Matcher mat1 = pat1.matcher(line);
        int pl = line.indexOf("(");
        int pl1 = line.indexOf(")");
        int pl2 = line.indexOf("(", pl+1);        
        if((pl!=-1 & pl1==-1)|(pl==-1 & pl1!=-1)|(mat.find())|(mat1.find())|(pl2<pl1 & pl2!=-1)){//проверка на корректность введенных данных (не возможны пробелы между цифрами; 
            System.out.println("Invalid data!");                                        //буквы вместо цифр; скобки в скобках; наличие только одной из скобок)
        }else { 
            line = line.replaceAll("\\s+","");
            int size = line.length(); 
            for(int i=0; i<size; i++){
                int  place= line.indexOf("(");
                if (place!=-1){                    
                    int place2 = line.indexOf(")");                        
                    lineRes = Calculator1.Parenthesis(line, place, place2);                     
                    line = line.substring(0, place)+lineRes+line.substring(place2+1, line.length());                    
                    size = line.length();
                    i=place;                    
                }else{
                    i=size;
                    ArrayList<String> num = Calculator1.Number(line);
                    ArrayList<String> sign = Calculator1.MathSigns(line);
                    String res = Calculator1.Action(num, sign);
                    System.out.println("result of calculation= "+res);
                }
            }      
        }
    }
    public static ArrayList<String> Number(String newLine){//выбираем из строки числа
        int sizeS=0;
        String[] num = newLine.split("[^\\d|.,]");
        ArrayList<String> num1 = new ArrayList<String>();
        for(int i=0; i<num.length; i++){
            if ("".equals(num[i])){
                num[i+1]="-"+num[i+1];
            }
            num1.add(num[i]);                              
        }
        sizeS = num1.size();
        for (int i=0; i<sizeS; i++){
            num1.remove("");
        }
        return num1;
    }
    public static ArrayList<String> MathSigns(String newLine){//выбираем арифметические действия
        int sizeS=0;
        String[] sign = newLine.split("[\\d.,]");
        ArrayList<String> sign1 = new ArrayList<String>(); 
                        
        for(int i=0; i<sign.length; i++){
            switch(sign[i]){
                case "^-": sign[i]="^"; break;
                case "*-": sign[i]="*"; break;
                case "/-": sign[i]="/"; break;
                case "+-": sign[i]="+"; break;
                case "--": sign[i]="-"; break;                
            }
            sign1.add(sign[i]);                        
        }
        sizeS = sign1.size();
        for (int i=0; i<sizeS; i++){
            sign1.remove("");
        }                   

        return sign1;                
    }    
    
    public static String Parenthesis(String line, int place1, int place2){//вычисляем выражение в скобках
        String numSign = null, newLine;        
                if (place1!=-1){                    
                    if (place2!=-1){
                        newLine = line.substring(place1+1, place2); 
                        ArrayList<String> num = Calculator1.Number(newLine);
                        ArrayList<String> sign = Calculator1.MathSigns(newLine);
                        numSign = Calculator1.Action(num, sign);                        
                        //System.out.println();                       
                    } else {System.out.println("parenthesis missing!");}
                }else{}
        return numSign;
    }
    public static  String Action(ArrayList<String> num, ArrayList<String> sign){//выполняем арифметические действия, в зависимости от приоритета       
        String result = null;
        BigDecimal bigDec = null, bigDec1 = null;       
        int Int = 0;
        for(int i=0; i<sign.size(); i++){
            if("^".equals(sign.get(i))){                
                if(num.get(i).indexOf(",")!=-1){num.get(i).replace(",", ".");}
                if(num.get(i+1).indexOf(",")!=-1){num.get(i).replace(",", ".");}
                bigDec = new BigDecimal(num.get(i));
                try{
                    Int = Integer.parseInt(num.get(i+1));                     
                }catch (NumberFormatException e){
                    System.out.println("the construction of a decimal degree!");
                }
                if (Int<0){
                    Int = 0-Int;
                    BigDecimal bigD = new BigDecimal("1");                    
                    bigDec = bigD.divide(bigDec.pow(Int), 8, BigDecimal.ROUND_HALF_UP);//округление, связанное с возможной бесконечностью цифр                    
                }else{
                    bigDec = bigDec.pow(Int);
                }
                num.remove(i);
                num.remove(i);
                num.add(i, bigDec.toString());
                sign.remove(i);
                i=0;
            }
        }        
        for(int i=0; i<sign.size(); i++){
            if("*".equals(sign.get(i))|"/".equals(sign.get(i))){
                if(num.get(i).indexOf(",")!=-1){num.get(i).replace(",", ".");}
                if(num.get(i+1).indexOf(",")!=-1){num.get(i).replace(",", ".");}
                bigDec = new BigDecimal(num.get(i));
                switch(sign.get(i)){
                    case "*": bigDec1 = new BigDecimal(num.get(i+1));
                              bigDec = bigDec.multiply(bigDec1);
                              break;
                    case "/": bigDec1 = new BigDecimal(num.get(i+1));
                              try{                                         
                                  bigDec = bigDec.divide(bigDec1, 8, BigDecimal.ROUND_HALF_UP);//округление, связанное с возможной бесконечностью цифр                                  
                              }
                              catch(ArithmeticException e){                                  
                                  return "arithmetic error!";//ошибка деления на ноль (например)
                              }
                              break;          
                }
                num.remove(i);
                num.remove(i);
                num.add(i, bigDec.toString());
                sign.remove(i);
                i=0;
            }
        }
        
        for(int i=0; i<sign.size(); i++){
            if("+".equals(sign.get(i))|"-".equals(sign.get(i))){                
                if(num.get(i).indexOf(",")!=-1){num.get(i).replace(",", ".");}
                if(num.get(i+1).indexOf(",")!=-1){num.get(i).replace(",", ".");}
                bigDec = new BigDecimal(num.get(i));
                switch(sign.get(i)){
                    case "+": bigDec1 = new BigDecimal(num.get(i+1));
                              bigDec = bigDec.add(bigDec1);
                              break;
                    case "-": bigDec1 = new BigDecimal(num.get(i+1));
                              bigDec = bigDec.subtract(bigDec1);
                              break;          
                }
                num.remove(i);
                num.remove(i);
                num.add(i, bigDec.toString());
                sign.remove(i);
                i=0;
            }
        }
        result=num.get(0);
        return result;
    }
}
