package antifraud.service;

import antifraud.exception.SameUserRoleException;
import antifraud.exception.UserAlreadyExistException;
import antifraud.exception.UserNotFoundException;
import antifraud.exception.WrongUserRoleException;
import antifraud.model.StatusDTO;
import antifraud.model.user.UserStatusDTO;
import antifraud.model.user.*;
import antifraud.rep.UserRepository;
import antifraud.rep.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    private UserRoleFabric userRoleFabric;
    private final PasswordEncoder encoder;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserRoleFabric userRoleFabric, PasswordEncoder encoder,
                       UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.userRoleFabric = userRoleFabric;
        this.encoder = encoder;
        this.userRoleRepository = userRoleRepository;
    }

    public User registerUser(User user) {

        if (isUserExist(user.getUsername())) {
            throw new UserAlreadyExistException();
        }

        if (userRepository.count() == 0) {
            user.setRole(userRoleFabric.instanceOf(UserRoleType.ADMINISTRATOR));
            user.setAccountNonLocked(true);
        } else {
            user.setRole(userRoleFabric.instanceOf(UserRoleType.MERCHANT));
            user.setAccountNonLocked(false);
        }

        user.setUsername(user.getUsername().toLowerCase());
        user.setPassword(encoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public boolean isUserExist(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username).isPresent();
    }

    public List<User> getAllUsers() {
        return (ArrayList<User>) userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public UserStatusDTO deleteUser(User user) {
        String username = user.getUsername();
        userRepository.delete(user);
        return new UserStatusDTO(username, "Deleted successfully!");
    }

    public User changeUserRole(UserRoleChangeDTO dto) {
        UserRoleType changeTo = dto.getRole();

        if (!(changeTo == UserRoleType.MERCHANT || changeTo == UserRoleType.SUPPORT)) {
            throw new WrongUserRoleException();
        }

        User user = findUserByUsername(dto.getUsername());

        if (changeTo == user.getRole().getType()) {
            throw new SameUserRoleException();
        }

        user.setRole(userRoleFabric.instanceOf(dto.getRole()));
        return userRepository.save(user);
    }

    public StatusDTO changeUserStatus(UserLockChangeDTO dto) {
        User user = findUserByUsername(dto.getUsername());

        UserRoleType userRole = user.getRole().getType();

        if (userRole == UserRoleType.ADMINISTRATOR) {
            throw new WrongUserRoleException();
        }

        UserOperation operation = dto.getUserOperation();

        switch (operation) {
            case UNLOCK -> user.setAccountNonLocked(true);
            case LOCK -> user.setAccountNonLocked(false);
        }
        userRepository.save(user);
        return new StatusDTO("User %s %s".formatted(user.getUsername(), operation.name().toLowerCase()) + "ed!");
    }
}
