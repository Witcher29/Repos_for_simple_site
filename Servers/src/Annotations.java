import java.lang.annotation.*;

public class Annotations {
    public static void main(String[] args) {
        CustomAnnotatedEmployee ex = new CustomAnnotatedEmployee(14, "AAA");
        ex.getEmployeeDetails();

        Annotation companyAnnotation = ex.getClass().getAnnotation(Company.class);
        Company company = (Company) companyAnnotation;

        System.out.println("Company Name: " + company.name());
        System.out.println("Company City: " + company.city());

        //маркерные аннотации, другую не пишу, это просто пустая аннотация
        Class exClass = ex.getClass();
        if(exClass.isAnnotationPresent(Company.class)){
            System.out.println("Он был аннотирован");
        }
    }
}
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Company{
    String name() default "aaa";
    String city() default  "Moscow";
}
@Company(name = "Mine Comp", city = "Piter")
class CustomAnnotatedEmployee {
    private int id;
    private String name;
    public CustomAnnotatedEmployee(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public void getEmployeeDetails(){
        System.out.println("Employee Id: " + id);
        System.out.println("Employee Name: " + name);
    }
}