package lab1;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class Calculator implements Serializable {
    // Read/write property "number":
    private int number = 1;
    public int getNumber() { return number; }
    public void setNumber(int number) {
        this.number = number;
    }
    // Read-only property "result":
    private Integer result = 1;
    public Integer getResult() { return result; }
    // Method to square a number
    public void square() { result = number * number; }
    // Method to navigate to the second page
    public String bye() { return "index"; }
}