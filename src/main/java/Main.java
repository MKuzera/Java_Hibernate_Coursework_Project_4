import backend.ClassTeacher;
import backend.Teacher;
import db.Database;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {

        int selected = 0;
        while(true){
            System.out.println("Co zrobic?");
            System.out.println(getOpcje());
            int number = scanner.nextInt();
            break;


        }



        System.out.println("Hello world!");
        Database.init();
        //

        //Database.addClassTeacherToClassTeachers("Klasa trzecia",5);
         Database.addTeacherToClass("Mateusz","Kuzera",new Date(),"OBECNY",3000.0,9);

        for (Teacher teacher : Database.getTeachersFromDatabase()) {
            System.out.println(teacher.toString());
        }



    //    Database.addTeacherToClassTeacher(8,11);
        List<ClassTeacher> classTeachersFromDatabase = Database.getClassTeachersWithTeachersFromDatabase();
        for (ClassTeacher cl: classTeachersFromDatabase) {
            System.out.println("XD");
            cl.summary();
        }

        Database.shutDown();

    }

    private static String getOpcje() {
        return "1 - Wyswietl klasy\n2 - Wyswietl ";
    }
}