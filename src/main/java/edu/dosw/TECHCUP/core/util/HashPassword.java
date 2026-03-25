package edu.dosw.TECHCUP.core.util;

import com.password4j.BcryptFunction;
import com.password4j.Hash;
import com.password4j.Password;

public class HashPassword {

/*
  @PostMapping("/login")
  public String userLogin(@RequestBody LoginDTO credentials) throws NullPointerException {
    if ((credentials.getUserName() == null) || (credentials.getPassword() == null)) {
      throw new NullPointerException();
    }

    UUID tokenGenerator = UUID.randomUUID();

    Hash hash = Password.hash(credentials.getPassword()).with(bcrypt);

    user = new User(credentials.getUserName(), hash.getResult(), tokenGenerator.toString());

    return tokenGenerator.toString();
  }

  */

 /*


@GetMapping("/verify")
  public Boolean verifyPassword(@RequestBody PasswordDTO password) {

    boolean verified = Password.check(password.getPassword(), user.getPassword()).with(bcrypt);

    return verified;

  }

*/

}
