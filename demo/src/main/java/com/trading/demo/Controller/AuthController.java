package com.trading.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trading.demo.config.JwtProvider;
import com.trading.demo.exception.UserException;
import com.trading.demo.model.TwoFactorOTP;
import com.trading.demo.model.Users;
import com.trading.demo.repository.UserRepository;
import com.trading.demo.request.LoginRequest;
import com.trading.demo.response.AuthResponse;
import com.trading.demo.service.CustomeUserServiceImplementation;
import com.trading.demo.service.EmailService;
import com.trading.demo.service.TwoFactorOtpService;
import com.trading.demo.service.UserService;
import com.trading.demo.service.VerificationService;
import com.trading.demo.service.WalletService;
import com.trading.demo.service.WatchlistService;
import com.trading.demo.utils.OtpUtils;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomeUserServiceImplementation customUserDetails;

	@Autowired
	private UserService userService;

	@Autowired
	private WatchlistService watchlistService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private VerificationService verificationService;

	@Autowired
	private TwoFactorOtpService twoFactorOtpService;

	@Autowired
	private EmailService emailService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(
			@RequestBody Users user) throws UserException {

		String email = user.getEmail();
		String password = user.getPassword();
		String fullName = user.getFullName();
		String mobile = user.getMobile();
		System.out.println(mobile + "mobile in user");

		Users isEmailExist = userRepository.findByEmail(email);

		if (isEmailExist != null) {

			throw new UserException("Email Is Already Used With Another Account");
		}

		// Create new user
		Users createdUser = new Users();
		createdUser.setEmail(email);
		createdUser.setFullName(fullName);
		createdUser.setMobile(mobile);
		createdUser.setPassword(passwordEncoder.encode(password));

		Users savedUser = userRepository.save(createdUser);

		watchlistService.createWatchList(savedUser);
		// walletService.createWallet(user);

		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = JwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Register Success");

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest)
			throws UserException, MessagingException {

		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		System.out.println(username + " ----- " + password);

		Authentication authentication = authenticate(username, password);

		Users user = userService.findUserByEmail(username);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = JwtProvider.generateToken(authentication);

		if (user.getTwoFactorAuth().isEnabled()) {
			AuthResponse authResponse = new AuthResponse();
			authResponse.setMessage("Two factor authentication enabled");
			authResponse.setTwoFactorAuthEnabled(true);

			String otp = OtpUtils.generateOTP();

			TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findByUser(user.getId());
			if (oldTwoFactorOTP != null) {
				twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
			}

			TwoFactorOTP twoFactorOTP = twoFactorOtpService.createTwoFactorOtp(user, otp, token);

			emailService.sendVerificationOtpEmail(user.getEmail(), otp);

			authResponse.setSession(twoFactorOTP.getId());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}

		AuthResponse authResponse = new AuthResponse();

		authResponse.setMessage("Login Success");
		authResponse.setJwt(token);

		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customUserDetails.loadUserByUsername(username);

		System.out.println("sign in userDetails - " + userDetails);

		if (userDetails == null) {
			System.out.println("sign in userDetails - null " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			System.out.println("sign in userDetails - password not match " + userDetails);
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySigningOtp(
			@PathVariable String otp,
			@RequestParam String id) throws Exception {

		TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);

		if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
			AuthResponse authResponse = new AuthResponse();
			authResponse.setMessage("Two factor authentication verified");
			authResponse.setTwoFactorAuthEnabled(true);
			authResponse.setJwt(twoFactorOTP.getJwt());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}
		throw new Exception("invalid otp");
	}

}
