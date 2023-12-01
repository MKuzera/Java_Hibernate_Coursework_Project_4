package backend;



import backend.Exceptions.MaxTeacherNumber;
import backend.Exceptions.ThisTeacherExists;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) {

        ClassContainer classContainer = new ClassContainer();
        classContainer.addClass("Pierwsza klasa",2);
        classContainer.addClass("Druga klasa",5);
        classContainer.addClass("Trzecia klasa",5);


        System.out.println("---test 1. Sprawdz czy moge dodac wiecej niz max osob---");
        Teacher teacher1 = new Teacher("Mateusz","Kuzera",new Date(2003,4,9),TeacherCondition.OBECNY,5000.0);
        Teacher teacher2 = new Teacher("Kacper","Kowalczyk",new Date(2002,2,4),TeacherCondition.CHORY,6000.0);
        Teacher teacher3 = new Teacher("Wiktor","Nowak",new Date(2001,3,19),TeacherCondition.OBECNY,8000.0);
        Teacher teacher4 = new Teacher("Ala","Osysko",new Date(2000,10,29),TeacherCondition.DELEGACJA,2000.0);
        Teacher teacher5 = new Teacher("Piotr","Ostry",new Date(2003,2,5),TeacherCondition.OBECNY,3000.0);
        try {

            classContainer.container.get("Pierwsza klasa").addTeacher(teacher1);
            classContainer.container.get("Pierwsza klasa").addTeacher(teacher2);
            classContainer.container.get("Pierwsza klasa").addTeacher(teacher3);
            classContainer.container.get("Pierwsza klasa").addTeacher(teacher4);
            classContainer.container.get("Pierwsza klasa").addTeacher(teacher5);
        } catch (ThisTeacherExists e) {
            System.out.println(e);
        } catch (MaxTeacherNumber e) {
            System.out.println(e);
        }

        System.out.println(classContainer.toString());
        classContainer.container.get("Pierwsza klasa").summary();

        System.out.println("\n---test 2. Dodaj taka sama osobe---");
        try {

            classContainer.container.get("Druga klasa").addTeacher(teacher1);
            classContainer.container.get("Druga klasa").addTeacher(teacher2);
            classContainer.container.get("Druga klasa").addTeacher(teacher3);
            classContainer.container.get("Druga klasa").addTeacher(teacher4);

            classContainer.container.get("Druga klasa").addTeacher(teacher1);

        }
        catch (MaxTeacherNumber e) {
            System.out.println(e);
        } catch (ThisTeacherExists e) {
            System.out.println(e);
        }

        System.out.println("\n---test 3. ClassContainer Dodawanie/usuwanie klass---");
        classContainer.addClass("Czwarta klasa",5);
        System.out.println(classContainer.toString());
        classContainer.removeClass("Czwarta klasa");
        System.out.println(classContainer.toString());

        System.out.println("\n---test 4. ClassContainer findEmpty ---");
        System.out.println(classContainer.toString());
        ArrayList<String> empty = classContainer.findEmpty();
        System.out.println(empty);

        System.out.println("\n---test 5. ClassTeacher Count by Condition---");
        System.out.println(classContainer.container.get("Druga klasa").countByCondition(TeacherCondition.OBECNY) + " obecny count w klasa druga (powinno wyjsc 2)");

        System.out.println("\n---test 6. ClassTeacher Sort by Last Name---");
        System.out.println(classContainer.container.get("Druga klasa").sortByLastName());

        System.out.println("\n---test 7. ClassTeacher Sort by Salary---");
        System.out.println(classContainer.container.get("Druga klasa").sortBySalary());

        System.out.println("\n---test 8. ClassTeacher Search Partial by lastName / firstName---");

        System.out.println(classContainer.container.get("Druga klasa").searchPartial("Mat"));
        System.out.println(classContainer.container.get("Druga klasa").searchPartial("Kow"));

        System.out.println("\n---test 9. ClassTeacher search by last name / firstName---");
        try {
            System.out.println(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"));
            System.out.println("Proba znalezienia kogos kogo nie ma: ");
            System.out.println(classContainer.container.get("Druga klasa").searchByLastName("Nie ma mnie"));
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("\n---test 10. change condition---");

        try {
            System.out.println(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"));
            classContainer.container.get("Druga klasa").changeCondition(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"),TeacherCondition.CHORY);
            System.out.println(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"));
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("\n---test 11. add Salary condition---");

        try {
            System.out.println(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"));
            classContainer.container.get("Druga klasa").addSalary(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"),500.0);
            System.out.println(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"));
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("\n---test 12. remove teacher---");
        try {
            classContainer.container.get("Druga klasa").summary();
            System.out.println("usuwanie");
            classContainer.container.get("Druga klasa").removeTeacher(classContainer.container.get("Druga klasa").searchByLastName("Kuzera"));
            classContainer.container.get("Druga klasa").summary();
            System.out.println("usuwanie nie istniejacej osoby");
            classContainer.container.get("Druga klasa").removeTeacher(classContainer.container.get("Druga klasa").searchByLastName("Kuzera2"));
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.println("\n---test 13. Collections.max ---");
        System.out.println("Wyswietla max salary uzywa coleccions.max oraz comparatora");
        System.out.println( classContainer.container.get("Druga klasa").maxSalary());


    }
}