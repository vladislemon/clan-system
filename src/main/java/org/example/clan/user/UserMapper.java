package org.example.clan.user;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public UserDto toDto(User user) {
        return new UserDto(user.getName(), user.getGold());
    }

    public List<UserDto> toDtos(Collection<User> users) {
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }
}
