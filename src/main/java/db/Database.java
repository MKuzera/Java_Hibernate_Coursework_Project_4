package db;

import backend.*;
import backend.Exceptions.MaxTeacherNumber;
import backend.Exceptions.ThisTeacherExists;
import jakarta.persistence.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import jakarta.persistence.criteria.*;
import org.hibernate.*;


public class Database {
    private static SessionFactory factory;
    private static ClassContainer classContainer;
    private static EntityManagerFactory entityManagerFactory;

    private static void loadDataFromDataBaseToClassConainer() {
        List<ClassTeacher> classTeachersWithTeachersFromDatabase = getClassTeachersWithTeachersFromDatabase();
        for (ClassTeacher cl : classTeachersWithTeachersFromDatabase) {
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

    public static boolean checkIfClassTeacherExists(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, id);
        entityManager.close();

        return classTeacher != null;
    }

    public static boolean checkIfTeacherExists(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Teacher teacher = entityManager.find(Teacher.class, id);
        entityManager.close();

        return teacher != null;
    }


    public static void addTeacherToClass(Teacher teacher, int groupID) throws Exception {
        if (!checkIfClassTeacherExists(groupID)) {
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

        if (!checkIfTeacherExists(idTeacher)) {
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

    public static void changeClassOfTeacher(int idTeacher, int idClass) throws Exception {

        if (!checkIfTeacherExists(idTeacher)) {
            throw new Exception("Nie ma takiego nauczyciela o takim id " + idTeacher);
        }
        if (!checkIfClassTeacherExists(idClass)) {
            throw new Exception("Nie ma takiej klasy o takim id " + idClass);
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        Teacher teacher = entityManager.find(Teacher.class, idTeacher);
        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, idClass);
        teacher.setClassTeacher(classTeacher);

        entityManager.merge(teacher);

        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public static void changeCondition(int idTeacher, String condition) throws Exception {

        if (!checkIfTeacherExists(idTeacher)) {
            throw new Exception("Nie ma takiego nauczyciela o takim id " + idTeacher);
        }

        if (!checkIfTeacherConditionExists(condition)) {
            throw new Exception("Nie ma takiej Condition");
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        for (TeacherCondition t : TeacherCondition.values()) {
            if (t.equals(TeacherCondition.valueOf(condition))) {

                Teacher teacher = entityManager.find(Teacher.class, idTeacher);
                teacher.setTeacherCondition(TeacherCondition.valueOf(condition));
                entityManager.merge(teacher);

            }
        }

        entityManager.getTransaction().commit();
        entityManager.close();


    }

    private static boolean checkIfTeacherConditionExists(String condition) {
        for (TeacherCondition t : TeacherCondition.values()) {
            if (t.equals(TeacherCondition.valueOf(condition))) {
                return true;
            }
        }
        return false;
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

        ClassTeacher classTeacher = new ClassTeacher(groupName, maxTeachers);

        entityManager.persist(classTeacher);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void removeTeacher(int teacherId) throws Exception {

        if (!checkIfTeacherExists(teacherId)) {
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

        if (!checkIfClassTeacherExists(classID)) {
            throw new Exception("Nie ma takiej klasy o takim id " + classID);
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, classID);
        entityManager.remove(classTeacher);


        // Zapisz zmiany
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public static void addTeacherToClassTeacher(int classTeacherId, int teacherId) {


        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, classTeacherId);

        Teacher teacher = entityManager.find(Teacher.class, teacherId);


        if (classTeacher != null && teacher != null) {

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

    public static void HQL_exportDataToSCV() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        TypedQuery<Teacher> query = entityManager.createQuery("SELECT t FROM Teacher t", Teacher.class);
        List<Teacher> teachers = query.getResultList();

        TypedQuery<ClassTeacher> query2 = entityManager.createQuery("SELECT t FROM ClassTeacher t", ClassTeacher.class);
        List<ClassTeacher> classTeachers = query2.getResultList();

        TypedQuery<Rate> query3 = entityManager.createQuery("SELECT t FROM Rate t", Rate.class);
        List<Rate> rates = query3.getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        try (FileWriter writer = new FileWriter("src/main/java/db/teachers.csv")) {

            writer.append("ID,First Name,Last Name,Year of Birth,Condition,Salary\n");


            for (Teacher teacher : teachers) {
                writer.append(String.valueOf(teacher.getId())).append(",");
                writer.append(teacher.getFirstName()).append(",");
                writer.append(teacher.getLastName()).append(",");
                writer.append(String.valueOf(teacher.getYearOfBirth())).append(",");
                writer.append(teacher.getTeacherCondition().toString()).append(",");
                writer.append(String.valueOf(teacher.getSalary())).append("\n");
            }

            System.out.println("Dane Nauczycieli zostały pomyślnie wyeksportowane do pliku CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("src/main/java/db/ClassTeachers.csv")) {

            writer.append("ID,Group Name,Max Teachers\n");

            // Dla każdej klasy nauczycieli
            for (ClassTeacher classTeacher : classTeachers) {
                writer.append(String.valueOf(classTeacher.getId())).append(",");
                writer.append(classTeacher.getGroupName()).append(",");
                writer.append(String.valueOf(classTeacher.getMaxTeachers())).append("\n");
            }

            System.out.println("Dane ClassTeacher zostały pomyślnie wyeksportowane do pliku CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("src/main/java/db/Rates.csv")) {

            writer.append("ID,Rating,Group ID,Date,Comment\n");


            for (Rate rate : rates) {
                writer.append(String.valueOf(rate.getId())).append(",");
                writer.append(String.valueOf(rate.getValue())).append(",");
                writer.append(String.valueOf(rate.getClassTeacher().getId())).append(",");
                writer.append(rate.getDate().toString()).append(",");
                writer.append(rate.getComment()).append("\n");
            }

            System.out.println("Dane Rates zostały pomyślnie wyeksportowane do pliku CSV.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static public void szukajPoNazwisku(String nazwisko){
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Teacher> criteriaQuery = criteriaBuilder.createQuery(Teacher.class);
            Root<Teacher> teacherRoot = criteriaQuery.from(Teacher.class);


            criteriaQuery.where(criteriaBuilder.like(teacherRoot.get("lastName"), nazwisko));

            criteriaQuery.select(teacherRoot).distinct(true);

            List<Teacher> teachers = entityManager.createQuery(criteriaQuery).getResultList();

            for (Teacher teacher : teachers) {
                System.out.println("Teacher: " + teacher.getFirstName() + " " + teacher.getLastName());

            }
        }
        catch(Exception e){
            System.out.println(e);

        }finally {
            entityManager.close();
            entityManagerFactory.close();
        }

    }
    static public void dodajRate(int idKlasy, Rate rate) throws Exception {
        if (!checkIfClassTeacherExists(idKlasy)) {
            throw new Exception("No class of that id");
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        ClassTeacher classTeacher = entityManager.find(ClassTeacher.class, idKlasy);
        classTeacher.addRate(rate);

        entityManager.persist(rate);


        entityManager.getTransaction().commit();
        entityManager.close();

    }

    static public void criteriaDisplay() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<ClassTeacher> criteriaQuery = criteriaBuilder.createQuery(ClassTeacher.class);
            Root<ClassTeacher> classTeacherRoot = criteriaQuery.from(ClassTeacher.class);

            // Dołącz nauczycieli do klasy za pomocą left join
            Join<ClassTeacher, Teacher> teacherJoin = classTeacherRoot.join("teacherList", JoinType.LEFT);

            criteriaQuery.select(classTeacherRoot).distinct(true);

            List<ClassTeacher> classes = entityManager.createQuery(criteriaQuery).getResultList();

            for (ClassTeacher classTeacher : classes) {
                System.out.println("ID:" + classTeacher.getId() +" klasa" + classTeacher.getGroupName());

                List<Teacher> teachers = classTeacher.getTeacherListV();
                for (Teacher teacher : teachers) {
                    System.out.println("  Teacher: " + teacher.getFirstName() + " " + teacher.getLastName());
                }
            }
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
    }

    public static void displayAverageSalaryByGroup() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Teacher> teacherRoot = criteriaQuery.from(Teacher.class);
        Join<Teacher, ClassTeacher> classTeacherJoin = teacherRoot.join("classTeacher");

        criteriaQuery.multiselect(
                classTeacherJoin.get("groupName"),
                criteriaBuilder.avg(teacherRoot.get("salary"))
        );
        criteriaQuery.groupBy(classTeacherJoin.get("groupName"));

        List<Object[]> results = entityManager.createQuery(criteriaQuery).getResultList();

        for (Object[] result : results) {
            System.out.println("Group: " + result[0] + ", Average Salary: " + result[1]);
        }

        entityManager.close();
    }

    public static void displayAverageRateByGroup() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);

        Root<Rate> rateRoot = criteriaQuery.from(Rate.class);
        Join<Rate, ClassTeacher> classTeacherJoin = rateRoot.join("classTeacher");

        criteriaQuery.multiselect(
                classTeacherJoin.get("groupName").alias("groupName"),
                criteriaBuilder.count(rateRoot.get("id")).alias("count"),
                criteriaBuilder.avg(rateRoot.get("value")).alias("average")
        );

        criteriaQuery.groupBy(classTeacherJoin.get("groupName"));

        TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery);
        query.getResultList().forEach(row -> {
            String groupName = (String) row[0];
            Long count = (Long) row[1];
            Double average = (Double) row[2];

            System.out.println("Group: " + groupName + ", Count: " + count + ", Average Value: " + average);
        });

        entityManager.close();
    }
}
