package org.example.electricstore.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer employeeId;
    private String employeeName;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate employeeBirthday;
    private String employeeAddress;
    private String employeePhone;
    private String employeeWork;
    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "position_id")
    private EmployeePosition employeePosition;
    @Column(name = "is_disabled", columnDefinition = "TINYINT(1)")
    private Boolean isDisabled ;
    @OneToOne(fetch = FetchType.EAGER,cascade = { CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name = "user_id")
    private User user ;

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                ", employeeBirthday=" + employeeBirthday +
                ", employeeAddress='" + employeeAddress + '\'' +
                ", employeePhone='" + employeePhone + '\'' +
                ", employeePosition=" + employeePosition +
                ", isDisabled=" + isDisabled +
                ", user=" + user +
                '}';
    }
}
