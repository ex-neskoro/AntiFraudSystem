package antifraud.model.user;

import antifraud.exception.RoleNotFoundException;
import antifraud.rep.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserRoleFabric {
    private List<UserRole> roles;
    @Autowired
    public UserRoleFabric(UserRoleRepository userRoleRepository) {

        long count = userRoleRepository.count();

        if (count == 0) {
            roles = Arrays.asList(
                    new UserRole("ROLE_ADMINISTRATOR", "ADMINISTRATIVE", UserRoleType.ADMINISTRATOR),
                    new UserRole("ROLE_MERCHANT", "BUSINESS", UserRoleType.MERCHANT),
                    new UserRole("ROLE_SUPPORT", "BUSINESS", UserRoleType.SUPPORT)
            );
            roles = (ArrayList<UserRole>) userRoleRepository.saveAll(roles);
        } else {
            roles = (ArrayList<UserRole>) userRoleRepository.findAll();
        }
        roles.sort(UserRole::compareTo);
    }

    public UserRole instanceOf(UserRoleType userRoleType) throws RoleNotFoundException {
        return switch (userRoleType) {
            case ADMINISTRATOR -> roles.get(0);
            case MERCHANT -> roles.get(1);
            case SUPPORT -> roles.get(2);
            default -> throw new RoleNotFoundException();
        };
    }
}
