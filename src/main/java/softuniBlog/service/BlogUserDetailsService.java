package softuniBlog.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import softuniBlog.entity.User;
import softuniBlog.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@Service("blogUserDetailsService")
public class BlogUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	public BlogUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid User");
		} else {
			Set<GrantedAuthority> grantedAuthorities = user.getRoles()
					.stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.collect(Collectors.toSet());

			return new org
					.springframework
					.security
					.core
					.userdetails
					.User(user.getUserName(), user.getPassword(), grantedAuthorities);
		}
	}
}