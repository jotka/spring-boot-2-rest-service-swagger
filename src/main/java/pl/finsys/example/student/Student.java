package pl.finsys.example.student;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Details about the student. ")
class Student {
    @Id
    @GeneratedValue
    private Long id;

    @ApiModelProperty(notes = "Name should have at least 2 characters")
    @Size(min = 2, message = "Name should have at least 2 characters")
    private String name;
    private String passportNumber;
}
