package com.trading.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trading.demo.config.JwtProvider;
import com.trading.demo.domain.VerificationType;
import com.trading.demo.exception.UserException;
import com.trading.demo.model.TwoFactorAuth;
import com.trading.demo.model.Users;
import com.trading.demo.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Users findUserProfileByJwt(String jwt) throws UserException {
		String email = JwtProvider.getEmailFromJwtToken(jwt);

		Users user = userRepository.findByEmail(email);

		if (user == null) {
			throw new UserException("user not exist with email " + email);
		}
		return user;
	}

	@Override
	public Users findUserByEmail(String username) throws UserException {

		Users user = userRepository.findByEmail(username);

		if (user != null) {

			return user;
		}

		throw new UserException("user not exist with username " + username);
	}

	@Override
	public Users findUserById(Long userId) throws UserException {
		Optional<Users> opt = userRepository.findById(userId);

		if (opt.isEmpty()) {
			throw new UserException("user not found with id " + userId);
		}
		return opt.get();
	}

	@Override
	public Users verifyUser(Users user) throws UserException {
		user.setVerified(true);
		return userRepository.save(user);
	}

	@Override
	public Users enabledTwoFactorAuthentication(
			VerificationType verificationType, String sendTo, Users user) throws UserException {
		TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
		twoFactorAuth.setEnabled(true);
		twoFactorAuth.setSendTo(verificationType);

		user.setTwoFactorAuth(twoFactorAuth);
		return userRepository.save(user);
	}

	@Override
	public Users updatePassword(Users user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		return userRepository.save(user);
	}

	@Override
	public void sendUpdatePasswordOtp(String email, String otp) {

	}

}
