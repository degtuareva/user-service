package edu.online.messenger.util;

import edu.online.messenger.model.entity.User;
import lombok.Builder;

@Builder(setterPrefix = "with")
public class UserTestBuilder {
    @Builder.Default
    private Long id = 5L;

    public User buildUser() {
        User user = new User();
        user.setId(id);
        return user;
    }
}