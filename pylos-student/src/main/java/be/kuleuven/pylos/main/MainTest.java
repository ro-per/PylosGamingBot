package be.kuleuven.pylos.main;

public class MainTest {
    public static void main(String[] args) {
        int i=0;
        int a=1;
        int b=2;


        if(i==0){
            a=0;
            System.out.println("I");

        }
        if(i==0){
            i=1;
            System.out.println("II");

        }
        if(i==0){
            System.out.println("III");
            System.out.println(i);
            i=0;
        }
        if(i==0){
            System.out.println("IV");
            System.out.println(i);
        }
    }
}
