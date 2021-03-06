package br.com.caelum.feel.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	public static String hash(String password){
		return new BCryptPasswordEncoder().encode(password);
	}
	
	public static void main(String[] args) {
		System.out.println(PasswordEncoder.hash("muleac37"));
	}
}
