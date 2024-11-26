package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private String name;
    private String address;
    private String age;
    private String tags;
    private String actions;

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", age='" + age + '\'' +
                ", tags='" + tags + '\'' +
                ", actions='" + actions + '\'' +
                '}';
    }
}
