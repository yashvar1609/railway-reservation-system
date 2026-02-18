package com.yash.railway.reservation.util;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {


    public static String hashPassword(String password){
        String passTosignUp;
        passTosignUp = BCrypt.hashpw(password, BCrypt.gensalt());
        return passTosignUp;
    }

    public static boolean checkPassword(String password, String hashedPassword){
        return BCrypt.checkpw(password, hashedPassword);
    }



}
