package dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DogDTO {

    private String id;
    private String name;
    private String breed;
    private String gender;
    private int age;
}