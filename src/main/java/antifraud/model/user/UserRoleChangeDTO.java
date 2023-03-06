package antifraud.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRoleChangeDTO {

    @JsonProperty("username")
    private String username;
    @JsonProperty("role")
    private UserRoleType role;

    public UserRoleChangeDTO(String username, UserRoleType role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRoleType getRole() {
        return role;
    }

    public void setRole(UserRoleType role) {
        this.role = role;
    }
}
