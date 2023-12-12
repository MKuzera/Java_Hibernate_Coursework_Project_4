package db;

import backend.ClassContainer;
import backend.ClassTeacher;
import backend.Exceptions.MaxTeacherNumber;
import backend.Exceptions.ThisTeacherExists;
import backend.Teacher;

import backend.TeacherCondition;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;


public class Database {
    private static ClassContainer classContainer;
    private static EntityManagerFactory entityManagerFactory;

    private static void loadDataFromDataBaseToClassConainer(){
        List<ClassTeacher> classTeachersWithTeachersFromDatabase = getClassTeachersWithTeachersFromDatabase();
        for (ClassTeacher cl: classTeachersWithTeachersFromDatabase) {
            classContainer.addClass(cl);
        }
    }
    public static void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("myPersistenceUnit");
        classContainer = new ClassContainer();
        loadDataFromDataBaseToClassConainer();
    }

    public static void shutDown() throws Exception {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    public static boolean checkIfClassTeacherExists(int id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, id);
        entityManager.close();

        return classTeacher != null;
    }

    public static boolean checkIfTeacherExists(int id){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Teacher teacher = entityManager.find(Teacher.class, id);
        entityManager.close();

        return teacher != null;
    }



    public static void addTeacherToClass(Teacher teacher, int groupID) throws Exception {
        if(!checkIfClassTeacherExists(groupID)) {
            throw new Exception("Nie ma takiej klasy o takim id " + groupID);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(teacher);

        entityManager.getTransaction().commit();
        entityManager.close();

        int id = teacher.getId();
        addTeacherToClassTeacher(groupID, id);
    }
    public static void changeSalary(int idTeacher, double salary) throws Exception {

        if(!checkIfTeacherExists(idTeacher)){
            throw new Exception("Nie ma takiego nauczyciela o takim id " + idTeacher);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Teacher teacher = entityManager.find(Teacher.class, idTeacher);
        teacher.setSalary(salary);
        entityManager.merge(teacher);

        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public static void changeCondition(int idTeacher, String condition) throws Exception {

        if(!checkIfTeacherExists(idTeacher)){
            throw new Exception("Nie ma takiego nauczyciela o takim id " + idTeacher);
        }
        for (TeacherCondition t: TeacherCondition.values()) {
            if(!t.equals(TeacherCondition.valueOf(condition))){
                throw new Exception("Bad condition");
            }
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Teacher teacher = entityManager.find(Teacher.class, idTeacher);
        teacher.setTeacherCondition(TeacherCondition.valueOf(condition));
        entityManager.merge(teacher);

        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public static List<ClassTeacher> getClassTeachersWithTeachersFromDatabase() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        TypedQuery<ClassTeacher> query = entityManager.createQuery(
                "SELECT DISTINCT ct FROM ClassTeacher ct " +
                        "LEFT JOIN FETCH ct.teacherList",
                ClassTeacher.class
        );

        List<ClassTeacher> classTeachers = query.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        return classTeachers;
    }


    public static void addClassTeacherToClassTeachers(String groupName, int maxTeachers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        ClassTeacher classTeacher = new ClassTeacher(groupName,maxTeachers);

        entityManager.persist(classTeacher);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void removeTeacher(int teacherId) throws Exception {

        if(!checkIfTeacherExists(teacherId)){
            throw new Exception("Nie ma takiego nauczyciela o takim id " + teacherId);
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Teacher teacher = entityManager.find(Teacher.class, teacherId);
        entityManager.remove(teacher);

        // Zapisz zmiany
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void removeClass(int classID) throws Exception {

        if(!checkIfClassTeacherExists(classID)){
            throw new Exception("Nie ma takiej klasy o takim id " + classID);
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        ClassTeacher classTeacher= entityManager.find(ClassTeacher.class, classID);
        entityManager.remove(classTeacher);


        // Zapisz zmiany
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void addTeacherToClassTeacher(int classTeacherId, int teacherId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        // Pobierz referencję do obiektu ClassTeacher
        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, classTeacherId);

        // Pobierz referencję do obiektu Teacher
        Teacher teacher = entityManager.find(Teacher.class, teacherId);

        // Sprawdź, czy obiekt ClassTeacher i Teacher zostały znalezione
        if (classTeacher != null && teacher != null) {



            // Dodaj nauczyciela do listy w ClassTeacher
            try {
                classTeacher.addTeacher(teacher);
                teacher.setClassTeacher(classTeacher);
            } catch (ThisTeacherExists e) {
                System.out.println(e);
            } catch (MaxTeacherNumber e) {
                System.out.println(e);
            }
        } else {
            System.out.println("ClassTeacher or Teacher not found.");
        }

        // Zapisz zmiany
        entityManager.getTransaction().commit();
        entityManager.close();
    }


}
