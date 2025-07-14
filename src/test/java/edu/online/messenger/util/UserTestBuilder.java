package edu.online.messenger.util;

import edu.online.messenger.constant.RoleName;
import edu.online.messenger.model.dto.UserDto;
import edu.online.messenger.model.entity.User;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(setterPrefix = "with")
public class UserTestBuilder {

    @Builder.Default
    private Long id = 5L;
    @Builder.Default
    private String login = "defaultLogin";

    @Builder.Default
    private String password = "defaultPassword";

    @Builder.Default
    private RoleName role = RoleName.USER;

    @Builder.Default
    private LocalDateTime lastVisitDate = LocalDateTime.now();

    @Builder.Default
    private LocalDateTime createDate = LocalDateTime.now();

    public User buildUser() {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setPassword(password);
        user.setRole(role);
        user.setLastVisitDate(lastVisitDate);
        user.setCreateDate(createDate);
        return user;
    }

    public UserDto buildUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setLogin(login);
        userDto.setPassword(password);
        userDto.setRole(role);
        userDto.setLastVisitDate(lastVisitDate);
        userDto.setCreateDate(createDate);
        return userDto;
    }
}