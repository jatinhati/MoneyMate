package org.example.MoneyManager.service;

import lombok.RequiredArgsConstructor;
import org.example.MoneyManager.dto.AuthDTO;
import org.example.MoneyManager.entity.ProfileEntity;
import org.example.MoneyManager.repositories.ProfileRepository;
import org.example.MoneyManager.dto.ProfileDTO;
import org.example.MoneyManager.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public ProfileDTO registerProfile(ProfileDTO profileDto){
        ProfileEntity newProfile = toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile = profileRepository.save(newProfile);
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your account";
        String body = "Dear " + newProfile.getFullName() + ",\n\n" +
                "Thank you for registering. Please click the following link to activate your account:\n" +
                activationLink + "\n\n" +
                "Best regards,\n" +
                "MoneyManager Team";
        emailService.sendEmail(newProfile.getEmail(),subject,body);
        return toDto(newProfile);
    }


    public ProfileEntity toEntity(ProfileDTO profileDto){
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .password(passwordEncoder.encode(profileDto.getPassword()))
                .profileImageUrl(profileDto.getProfileImageUrl())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();

    }
    public ProfileDTO toDto(ProfileEntity profileEntity){
        return ProfileDTO.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();

    }

    public boolean activationProfile(String token) {
        ProfileEntity profile = profileRepository.findByActivationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid activation token"));
        profile.setIsActive(true);
        profile.setActivationToken(null);
        profileRepository.save(profile);
        return true;
    }

    public boolean isProfileActive(String email) {
        ProfileEntity profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found with email: " + email));
        return profile.getIsActive() != null && profile.getIsActive();
    }

    public ProfileEntity getCurrentProfile(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return profileRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + authentication.getName()));
    }

    public ProfileDTO getPublicProfile(String email) {
        ProfileEntity currentUser = null;
        if (email == null) {
            currentUser = getCurrentProfile();
        } else {
            currentUser = profileRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Profile not found with email: " + email));
        }
        return ProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .profileImageUrl(currentUser.getProfileImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();
    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDto) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDto.getEmail(), authDto.getPassword()));
            String token = jwtUtil.generateToken(authDto.getEmail());
            return Map.of("token", token,
                    "User",getPublicProfile(authDto.getEmail())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }
}



