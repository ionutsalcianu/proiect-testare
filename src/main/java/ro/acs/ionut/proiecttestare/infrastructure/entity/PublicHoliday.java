package ro.acs.ionut.proiecttestare.infrastructure.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "public_holiday")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PublicHoliday {

    @Id
    @Column(name = "ph_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ph_date")
    private LocalDate date;

    @Column(name = "ph_name")
    private String name;
}
