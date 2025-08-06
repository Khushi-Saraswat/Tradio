package com.trading.demo.service;

import com.trading.demo.domain.VerificationType;
import com.trading.demo.exception.UserException;
import com.trading.demo.model.Users;

public interface UserService {

	public Users findUserProfileByJwt(String jwt) throws UserException;

	public Users findUserByEmail(String email) throws UserException;

	public Users findUserById(Long userId) throws UserException;

	public Users verifyUser(Users user) throws UserException;

	public Users enabledTwoFactorAuthentication(VerificationType verificationType,
			String sendTo, Users user) throws UserException;

	// public List<User> getPenddingRestaurantOwner();

	Users updatePassword(Users user, String newPassword);

	void sendUpdatePasswordOtp(String email, String otp);

	// void sendPasswordResetEmail(User user);
}
