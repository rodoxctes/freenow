package com.mytaxi.domainobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(
    name = "manufacturer",
    uniqueConstraints = @UniqueConstraint(name = "uc_name", columnNames = {"name"})
)
public class ManufacturerDO {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated;


    @Column(nullable = false)
    @NotNull(message = "Name can not be null!")
    private String name;

    public ManufacturerDO(String name) {
        this.name = name;
        this.dateCreated = ZonedDateTime.now();
    }

    public ManufacturerDO(long id, String name) {
        this.id = id;
        this.name = name;
        this.dateCreated = ZonedDateTime.now();
    }

}
