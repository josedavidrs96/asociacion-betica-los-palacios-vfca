package com.pena.app.service;

import com.pena.app.entity.AppUser;
import com.pena.app.entity.Member;
import com.pena.app.entity.Role;
import com.pena.app.repository.AppUserRepository;
import com.pena.app.repository.MemberRepository;
import com.pena.app.repository.RoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository appUserRepository,
                       RoleRepository roleRepository,
                       MemberRepository memberRepository,
                       PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser u = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Set<GrantedAuthority> auths = u.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .collect(Collectors.toSet());
        boolean enabled = u.isEnabled();
        // accountNonExpired, credentialsNonExpired, accountNonLocked = true
        return new User(u.getUsername(), u.getPassword(), enabled, true, true, true, auths);
    }

    @Transactional
    public AppUser registerAbonado(String fullName, String phone, String email, String address, String username, String rawPassword) {
        if (appUserRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Member member = new Member();
        member.setFullName(fullName)
                .setPhone(phone)
                .setEmail(email)
                .setAddress(address)
                .setEmailVerified(false)
                .setAddressVerified(false);
        memberRepository.save(member);

        Role abonado = roleRepository.findByName("ABONADO")
                .orElseThrow(() -> new IllegalStateException("Rol ABONADO no encontrado"));

        AppUser user = new AppUser(username, passwordEncoder.encode(rawPassword));
        // El usuario queda deshabilitado hasta validaciones
        user.setEnabled(false);
        user.setMember(member).addRole(abonado);
        return appUserRepository.save(user);
    }

    public AppUser findByUsernameOrThrow(String username) {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
