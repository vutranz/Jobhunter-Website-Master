package vn.thecode.jobhunter.domain.response;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.thecode.jobhunter.util.constant.genderEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
    private long id;
    private String email;
    private String name;
    @Enumerated(EnumType.STRING)
    private genderEnum gender;
    private String address;
    private int age;

    private Instant updatedAt;
    private Instant createdAt;
    private CompanyUser company;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
