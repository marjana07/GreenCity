package greencity.security.jwt;

import static greencity.constant.ErrorMessage.USER_NOT_FOUND_BY_EMAIL;

import greencity.entity.User;
import greencity.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService}.
 *
 * @author Nazar Stasyuk
 * @version 1.0
 */
@Service
@AllArgsConstructor
@Slf4j
public class JwtUserDetailService implements UserDetailsService {
    private UserService userService;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("begin");
        User user = userService.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND_BY_EMAIL + email);
        }
        log.info("end");
        return new JwtUser(user);
    }
}
