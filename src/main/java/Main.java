import backend.ClassTeacher;
import backend.Teacher;
import backend.TeacherCondition;
import db.Database;
import db.Rate;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args)  {
        Teacher teacher1 = new Teacher("Mateusz","Kuzera",new Date(2003,4,9), "OBECNY",5000.0);
        Teacher teacher2 = new Teacher("Kacper","Kowalczyk",new Date(2002,2,4),"CHORY",6000.0);
        Teacher teacher3 = new Teacher("Wiktor","Nowak",new Date(2001,3,19),"OBECNY",8000.0);
        Teacher teacher4 = new Teacher("Ala","Osysko",new Date(2000,10,29),"DELEGACJA",2000.0);
        Teacher teacher5 = new Teacher("Piotr","Ostry",new Date(2003,2,5),"OBECNY",3000.0);


        Database.init();

        List<ClassTeacher>  classTeachersFromDatabase;
        int selected = 0;
        boolean loop = true;
        while(loop){
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
                    break;
                case 3:
                    // dodaj nowa klase
                    Database.addClassTeacherToClassTeachers("Klasa 5",5);
                    break;
                case 4:
                    // dodaj nauczyciela do grupy
                    try {

                        Database.addTeacherToClass(teacher2,2);
                        Database.addTeacherToClass(teacher3,9);
                        Database.addTeacherToClass(teacher4,10);
                        Database.addTeacherToClass(teacher5,10);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;

                case 5:
                    // zmien wynagrodzenie
                    try {
                        Database.changeSalary(12,12000.0);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 6:
                    // zmien stan nauczyciela
                    try {
                        Database.changeCondition(12,"DELEGACJA");
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 7:
                    // usun klase
                    try {
                        Database.removeClass(11);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 8:
                    // usun nauczyciela
                    try {
                        Database.removeTeacher(11);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 9:
                    // zmien klase nauczycielowi
                    try {
                        Database.changeClassOfTeacher(5,2);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 10:
                    // export teachers do scv uzywajac HQL
                    try {
                        Database.HQL_exportDataToSCV();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 11:
                    // dodaj rate
                    try {

                        Database.dodajRate(9, new Rate(2,new Date(),"test2"));
                        Database.dodajRate(10, new Rate(3,new Date(),"test2"));

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    break;
                case 12:
                    // citeria join left ClassTEacher oraz Teachers

                    Database.criteriaDisplay();
                    break;
                case 13:
                    // srednia salary na grupe
                    Database.displayAverageSalaryByGroup();
                    break;

                case 14:
                    // srednia salary na grupe
                    Database.displayAverageRateByGroup();
                    break;
                default:
                    loop = false;
                    break;
            }

        }


        try {
            Database.shutDown();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static String getOpcje() {
        return  "\nInny wybor - wyjdz" +
                "\n1 - Wyswietl klasy" +
                "\n2 - Wyswietl dana klase" +
                "\n3 - Dodaj klase" +
                "\n4 - Dodaj do klasy nauczyciela" +
                "\n5 - Zmien salary" +
                "\n6 - Zmien condition" +
                "\n7 - Usun klase" +
                "\n8 - Usun nauczyciela" +
                "\n9 - Zmien klase nauczycielowi" +
                "\n10 - Export danych do csv HQL"+
                "\n11 - Dodaj Rate"+
                "\n12 - Uzycie Criteria wyswietl"+
                "\n13 - Uzycie Criteria average salary per class"+
                "\n14 - Uzycie Criteria average rate per class";
    }
}