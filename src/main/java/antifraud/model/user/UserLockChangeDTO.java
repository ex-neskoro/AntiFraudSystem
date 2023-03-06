package antifraud.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserLockChangeDTO {
    @JsonProperty("username")
    private String username;
    @JsonProperty("operation")
    private UserOperation userOperation;

    public UserLockChangeDTO(String username, UserOperation userOperation) {
        this.username = username;
        this.userOperation = userOperation;
    }

    public UserLockChangeDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserOperation getUserOperation() {
        return userOperation;
    }

    public void setUserOperation(UserOperation userOperation) {
        this.userOperation = userOperation;
    }
}
