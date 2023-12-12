import backend.ClassTeacher;
import backend.Teacher;
import backend.TeacherCondition;
import db.Database;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        Teacher teacher1 = new Teacher("Mateusz","Kuzera",new Date(2003,4,9), "OBECNY",5000.0);
        Teacher teacher2 = new Teacher("Kacper","Kowalczyk",new Date(2002,2,4),"CHORY",6000.0);
        Teacher teacher3 = new Teacher("Wiktor","Nowak",new Date(2001,3,19),"OBECNY",8000.0);
        Teacher teacher4 = new Teacher("Ala","Osysko",new Date(2000,10,29),"DELEGACJA",2000.0);
        Teacher teacher5 = new Teacher("Piotr","Ostry",new Date(2003,2,5),"OBECNY",3000.0);


        Database.init();

        List<ClassTeacher>  classTeachersFromDatabase;
        int selected = 0;
        while(true){
            System.out.println("Co zrobic?");
            System.out.println(getOpcje());
            int number = scanner.nextInt();

            switch(number){
                case 1:
                    // jedynie wyswietlanie wykorzysuje pobrane listy z bazy danych
                    classTeachersFromDatabase = Database.getClassTeachersWithTeachersFromDatabase();
                    for (ClassTeacher cl: classTeachersFromDatabase) {
                        System.out.println();
                        cl.summary();
                    }
                    break;
                case 2:
                    // jedynie wyswietlanie wykorzysuje pobrane listy z bazy danych
                    System.out.println("Podaj id klasy");
                    int select = scanner.nextInt();

                    classTeachersFromDatabase = Database.getClassTeachersWithTeachersFromDatabase();
                    for (ClassTeacher cl: classTeachersFromDatabase) {
                        if(cl.getId() == select)
                        {
                            cl.summary();
                            break;
                        }
                    }
                case 3:
                    // dodaj nowa klase
                    Database.addClassTeacherToClassTeachers("Klasa 5",5);
                    break;
                case 4:
                    // dodaj nauczyciela do grupy
                    Database.addTeacherToClass(teacher1,3);
                    break;

                case 5:
                    // zmien wynagrodzenie
                    Database.changeSalary(12,10000.0);
                    break;
                case 6:
                    // zmien stan nauczyciela
                    Database.changeCondition(12,"CHORY");
                    break;
                case 7:
                    // usun klase
                    Database.removeClass(2);
                   break;
                case 8:
                    // usun nauczyciela
                    Database.removeTeacher(12);
                    break;

                default:
                    break;
            }
            break;
        }


        Database.shutDown();
    }

    private static String getOpcje() {
        return  "\n1 - Wyswietl klasy" +
                "\n2 - Wyswietl dana klase" +
                "\n3 - Dodaj klase" +
                "\n4 - Dodaj do klasy nauczyciela" +
                "\n5 - Zmien salary" +
                "\n6 - Zmien condition" +
                "";
    }
}