package db;
import backend.ClassTeacher;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rates")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "value", nullable = false)
    private int value;

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }

    public ClassTeacher getClassTeacher() {
        return classTeacher;
    }

    @ManyToOne
    @JoinColumn(name = "classTeacher_id")
    private ClassTeacher classTeacher;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private Date date;

    @Column(name = "comment")
    private String comment;

    public Rate() {
    }

    public void setClassTeacher(ClassTeacher classTeacher) {
        this.classTeacher = classTeacher;
    }

    public Rate(int value, Date date, String comment) throws Exception {
        if(value < 0 || value > 6){
            throw new Exception("wartosc value ma byc od 0 do 6");
        }
        this.value = value;
        this.date = date;
        this.comment = comment;
    }




}