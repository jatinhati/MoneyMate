package org.example.MoneyManager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.MoneyManager.dto.AuthDTO;
import org.example.MoneyManager.entity.ProfileEntity;
import org.example.MoneyManager.repositories.ProfileRepository;
import org.example.MoneyManager.dto.ProfileDTO;
import org.example.MoneyManager.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${app.activation.url}")
    private String activationUrl;

    @Transactional
    public ProfileDTO registerProfile(ProfileDTO profileDto) {
        // Check if email already exists
        if (profileRepository.existsByEmail(profileDto.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Create and save the profile
        ProfileEntity newProfile = toEntity(profileDto);
        newProfile.setActivationToken(UUID.randomUUID().toString());
        newProfile.setIsActive(false); // Set to false until email is verified
        newProfile = profileRepository.save(newProfile);

        // Prepare activation email
        String activationLink = activationUrl + "/api/v1.0/activate?token=" + newProfile.getActivationToken();
        String subject = "Activate your MoneyManager account";
        String body = String.format("""
            Dear %s,

            Thank you for registering with MoneyManager. To complete your registration, 
            please click the following link to activate your account:

            %s

            This link will expire in 24 hours.

            If you did not create an account, please ignore this email.

            Best regards,
            MoneyManager Team
            """, newProfile.getFullName(), activationLink);

        // Send activation email asynchronously
        try {
            emailService.sendEmail(newProfile.getEmail(), subject, body);
        } catch (Exception e) {
            // Log the error but don't fail the registration
            // The user can request a new activation email if needed
            log.error("Failed to send activation email to: {}", newProfile.getEmail(), e);
        }

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



