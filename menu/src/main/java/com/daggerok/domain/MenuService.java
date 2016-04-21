package com.daggerok.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@SpringCloudApplication
public class MenuService {

    public static void main(String[] args) {
        SpringApplication.run(MenuService.class, args);
    }
}

@RepositoryRestResource
interface MenuRepository extends JpaRepository<Menu, Long> {}

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
class Menu implements Serializable {

    private static final long serialVersionUID = -6342892451052909972L;

    @Id
    @GeneratedValue
    Long id;

    @NonNull
    String content;
    LocalDateTime updated = LocalDateTime.now();
}
