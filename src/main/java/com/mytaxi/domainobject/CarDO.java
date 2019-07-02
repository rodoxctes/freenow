package com.mytaxi.domainobject;

import com.mytaxi.domainvalue.EngineType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(
    name = "car",
    uniqueConstraints = @UniqueConstraint(name = "uc_license_plate", columnNames = {"license_plate"})
)
public class CarDO
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false, name = "license_plate")
    @NotNull(message = "License plate can not be null!")
    private String licensePlate;

    @Min(1)
    @Column(nullable = false)
    @NotNull(message = "Seat count can not be null!")
    private int seatCount;

    @Column(nullable = false)
    private boolean convertible = false;

    @Column(nullable = false)
    private Boolean deleted = false;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EngineType engineType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manufacturer_id", nullable=false, updatable=false)
    private ManufacturerDO manufacturer;

    @Column(nullable = false)
    private boolean selected = false;


    public CarDO(String licensePlate, int seatCount, boolean convertible, boolean deleted, int rating, EngineType engineType, boolean selected)
    {
        this.licensePlate = licensePlate;
        this.convertible = convertible;
        this.seatCount = seatCount;
        this.deleted = deleted;
        this.rating = rating;
        this.engineType = engineType;
        this.selected = selected;
    }


    public CarDO(String licensePlate, int seatCount, EngineType engineType, ManufacturerDO manufacturer)
    {
        this.licensePlate = licensePlate;
        this.engineType = engineType;
        this.seatCount = seatCount;
        this.manufacturer = manufacturer;
    }

}
