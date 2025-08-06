package com.trading.demo.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.demo.domain.VerificationType;
import com.trading.demo.exception.UserException;
import com.trading.demo.model.ForgotPasswordToken;
import com.trading.demo.model.Users;
import com.trading.demo.model.VerificationCode;
import com.trading.demo.request.ResetPasswordRequest;
import com.trading.demo.request.UpdatePasswordRequest;
import com.trading.demo.response.ApiResponse;
import com.trading.demo.response.AuthResponse;
import com.trading.demo.service.EmailService;
import com.trading.demo.service.ForgotPasswordService;
import com.trading.demo.service.UserService;
import com.trading.demo.service.VerificationService;
import com.trading.demo.utils.OtpUtils;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private VerificationService verificationService;

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	private EmailService emailService;

	@GetMapping("/api/users/profile")
	public ResponseEntity<Users> getUserProfileHandler(
			@RequestHeader("Authorization") String jwt) throws UserException {

		Users user = userService.findUserProfileByJwt(jwt);
		user.setPassword(null);

		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<Users> findUserById(
			@PathVariable Long userId,
			@RequestHeader("Authorization") String jwt) throws UserException {

		Users user = userService.findUserById(userId);
		user.setPassword(null);

		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/api/users/email/{email}")
	public ResponseEntity<Users> findUserByEmail(
			@PathVariable String email,
			@RequestHeader("Authorization") String jwt) throws UserException {

		Users user = userService.findUserByEmail(email);

		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<Users> enabledTwoFactorAuthentication(
			@RequestHeader("Authorization") String jwt,
			@PathVariable String otp) throws Exception {

		Users user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationService.findUsersVerification(user);

		String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
				? verificationCode.getEmail()
				: verificationCode.getMobile();

		boolean isVerified = verificationService.VerifyOtp(otp, verificationCode);

		if (isVerified) {
			Users updatedUser = userService.enabledTwoFactorAuthentication(verificationCode.getVerificationType(),
					sendTo, user);
			verificationService.deleteVerification(verificationCode);
			return ResponseEntity.ok(updatedUser);
		}
		throw new Exception("wrong otp");

	}

	@PatchMapping("/auth/users/reset-password/verify-otp")
	public ResponseEntity<ApiResponse> resetPassword(
			@RequestParam String id,
			@RequestBody ResetPasswordRequest req) throws Exception {
		ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

		boolean isVerified = forgotPasswordService.verifyToken(forgotPasswordToken, req.getOtp());

		if (isVerified) {

			userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
			ApiResponse apiResponse = new ApiResponse();
			apiResponse.setMessage("password updated successfully");
			return ResponseEntity.ok(apiResponse);
		}
		throw new Exception("wrong otp");

	}

	@PostMapping("/auth/users/reset-password/send-otp")
	public ResponseEntity<AuthResponse> sendUpdatePasswordOTP(
			@RequestBody UpdatePasswordRequest req)
			throws Exception {

		Users user = userService.findUserByEmail(req.getSendTo());
		String otp = OtpUtils.generateOTP();
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();

		ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

		if (token == null) {
			token = forgotPasswordService.createToken(
					user, id, otp, req.getVerificationType(), req.getSendTo());
		}

		if (req.getVerificationType().equals(VerificationType.EMAIL)) {
			emailService.sendVerificationOtpEmail(
					user.getEmail(),
					token.getOtp());
		}

		AuthResponse res = new AuthResponse();
		res.setSession(token.getId());
		res.setMessage("Password Reset OTP sent successfully.");
		System.out.println(res + "res in send password");
		return ResponseEntity.ok(res);

	}

	@PatchMapping("/api/users/verification/verify-otp/{otp}")
	public ResponseEntity<Users> verifyOTP(
			@RequestHeader("Authorization") String jwt,
			@PathVariable String otp) throws Exception {

		Users user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationService.findUsersVerification(user);

		boolean isVerified = verificationService.VerifyOtp(otp, verificationCode);
		System.out.print(isVerified + "isVerified");
		if (isVerified) {
			verificationService.deleteVerification(verificationCode);
			Users verifiedUser = userService.verifyUser(user);
			return ResponseEntity.ok(verifiedUser);
		}
		throw new Exception("wrong otp");

	}

	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOTP(
			@PathVariable VerificationType verificationType,
			@RequestHeader("Authorization") String jwt)
			throws Exception {

		System.out.println(verificationType + "verificationType in backend");
		Users user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationService.findUsersVerification(user);

		if (verificationCode == null) {
			verificationCode = verificationService.sendVerificationOTP(user, verificationType);
		}

		if (verificationType.equals(VerificationType.EMAIL)) {
			emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
		}

		if (verificationType.equals(VerificationType.MOBILE)) {
			// emailService.sendVerificationOtpEmail(user.getEmail(),
			// verificationCode.getOtp());
		}

		return ResponseEntity.ok("Verification OTP sent successfully.");

	}

}
