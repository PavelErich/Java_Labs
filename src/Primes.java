public class Primes {
    public static void main(String[] args){
        for(int i = 1; i <= 100; i++)
            if(isPrime(i))
                System.out.print(i + " ");
    }
    //Функция принимает целое число и возвращает
    //true - если передаваемое число простое
    //false - если передаваемое число составно
    public static boolean isPrime(int n){
        if(n == 1) return false;
        //Достаточно пройти циклом от 2 до корня от числа
        //включительно. Т.к. если число составное, то
        //a * b = sqrt(n) * sqrt(n)
        //допустим a <= b и a <= sqrt(n),
        //тогда b >= sqrt(n), следовательно
        //если a > sqrt(n) и b > sqrt(n), то a * b > n
        //получается что на промежутке [2, sqrt(n)] мы
        //точно найдем делитель числа (если оно составное)
        double sqrt_n = Math.sqrt(n);
        for(int i = 2; i <= sqrt_n; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }
}