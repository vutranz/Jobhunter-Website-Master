package vn.thecode.jobhunter.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;

    // @JsonProperty("user")
    private UserLogin user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        private long id;
        private String email;
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetAccount {
        // @JsonProperty("user")
        private UserLogin user;
    }

}
