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
    name = "user",
    uniqueConstraints = @UniqueConstraint(name = "uc_usernameLogin", columnNames = {"username"})
)
public class UserDO
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime dateCreated = ZonedDateTime.now();

    @Column(nullable = false)
    @NotNull(message = "Username can not be null!")
    private String username;

    @Column(nullable = false)
    @NotNull(message = "Password can not be null!")
    private String password;

    @Column(nullable = false)
    private Boolean deleted = false;



    public UserDO(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.deleted = false;
    }


}
