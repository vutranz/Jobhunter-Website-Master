package vn.thecode.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.thecode.jobhunter.util.constant.genderEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private genderEnum gender;
    private String address;
    private int age;
    private Instant updatedAt;
    private CompanyUser company;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyUser {
        private long id;
        private String name;
    }
}
