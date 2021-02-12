public class Palindrome {
    public static void main(String[] args){
        for(int i = 0; i < args.length; i++){
            String str = args[i];
            System.out.print(str);
            //Сравниваем строчки и если они равны,
            //то рассматриваемое слово является палиндромом
            if(str.equals(getReverseStr(str)))
                System.out.println(" is Palindrome");
            else
                System.out.println(" is not Palindrome");
        }
    }
    //Функция принимает строку и возвращает
    //ее перевернутую версию
    public static String getReverseStr(String str){
        String res = "";
        //Начиная с конца переданной строки добавляем к
        //результирующей строке символы переданной
        for(int i = str.length() - 1; i >= 0; i--)
            res += str.charAt(i);
        return res;
    }
}