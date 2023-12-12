package backend;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;

public class ClassContainer {
    Map<String, ClassTeacher> container = new HashMap<>();
    public  Map<String, ClassTeacher> getContainer(){
        return container;
    }
    public void addClass(String groupName,Integer capacity){
        ClassTeacher classTeacher = new ClassTeacher(groupName, capacity);
        container.put(groupName,classTeacher);
    }
    public void addClass(ClassTeacher classTeacher){
        container.put(classTeacher.getGroupName() ,classTeacher);
    }
    public void removeClass(String groupName){
        container.remove(groupName);
    }
    public ArrayList<String> findEmpty(){
        Iterator<Map.Entry<String, ClassTeacher>> iterator = container.entrySet().iterator();
        ArrayList<String> listOfEmptyClasses = new ArrayList<>();
        while(iterator.hasNext()){

            Map.Entry<String, ClassTeacher> entry = iterator.next();
            ClassTeacher value = entry.getValue();
            if(value.getTeacherList().isEmpty()){

                listOfEmptyClasses.add(entry.getKey());
            }
        }
        return listOfEmptyClasses;
    }
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        Iterator<Map.Entry<String, ClassTeacher>> iterator = container.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, ClassTeacher> entry = iterator.next();
            Double percentFull = (double) entry.getValue().getTeacherList().size() / entry.getValue().getMaxTeachers().doubleValue() * 100.0;
            s.append(entry.getKey() + " ");
            s.append(percentFull.toString() + "% Full\n");
        }
        return s.toString();
    }
}
