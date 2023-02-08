package org.example.user;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

public class UserGen {
    public User def() {
        return new User("Tasha261280@ya.ru", "2611", "Tasha");
    }

    public User random() {
        String[] mails = new String[]{"@yandex.ru", "@ya.ru", "@gmail.com", "@mail.ru"};
        int randomMails = new Random().nextInt(mails.length);
        return new User(RandomStringUtils.randomAlphanumeric(6, 9) + mails[randomMails], RandomStringUtils.randomAlphanumeric(4, 10), RandomStringUtils.randomAlphabetic(3, 9));
    }
}
