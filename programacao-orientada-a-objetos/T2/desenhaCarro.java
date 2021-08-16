public class desenhaCarro{ 

   public static void main(String[] args){
   
System.out.print("    ____\n");
System.out.print(" __/  |_ \\_\n"); // \_\n");
System.out.print("|  _     _``-.       Carro popular\n");
System.out.print("'-(_)---(_)--'\n\n\n");

System.out.print("  ______\n");
System.out.print(" /|_||_\\`.__\n");
System.out.print("(   _    _ _\\ \n");
System.out.print("=`-(_)--(_)-'\n\n"); 


System.out.print("        __         \n");
System.out.print("      ~( @\\ \\   \n");
System.out.print("   _____]_[_/_>__   \n");
System.out.print("  / __ \\<> |  __ \\      \n");
System.out.print("=\\_/__\\_\\__|_/__\\_D     Ferrari\n");
System.out.print("   (__)      (__)    \n");


//String[] bike= new String[3];
//bike[0]="   __o\n";
//bike[1]=" _`\\<,_\n";
//bike[2]="(*)/ (*)\n\n";

String[] bike = {"   __o\n"," _`\\<,_\n","(*)/ (*)\n\n"};
System.out.print(bike[0]);
System.out.print(bike[1]);
System.out.print(bike[2]);

String giroBike = "    ";
for(int i=0;i<bike.length;i++)
	bike[i] = giroBike + bike[i]; 
System.out.print(bike[0]);
System.out.print(bike[1]);
System.out.print(bike[2]);

for(int i=0;i<bike.length;i++)
	bike[i] = giroBike + bike[i]; 
System.out.print(bike[0]);
System.out.print(bike[1]);
System.out.print(bike[2]);


    
/*
  ______
 /|_||_\`.__
(   _    _ _\
=`-(_)--(_)-' 
*/

System.out.print("   __o\n");    
System.out.print(" _`\\<,_       Bike\n");
System.out.print("(*)/ (*)\n\n\n");

System.out.print("   ,_oo\n");
System.out.print(".-/c-//::          Motocicleta\n");  
System.out.print("(_)'==(_)\n");



/*
   ,_oo
.-/c-//::  
(_)'==(_)

*/

/*
    ____
 __/  |_\_
|  _     _``-.
'-(_)---(_)--'
*/

/*
   __o    
 _`\<,_
(*)/ (*)

*/

/*
       __
     ~( @\ \
  _____]_[_/_>____
 /  __ \<>  |  __ \
=\_/__\_\___|_/__\_D
   (__)       (__)    


            _______
          _/\______\\__
         / ,-. -|-  ,-.`-.
         `( o )----( o )-'
           `-'      `-'


*/



}
}