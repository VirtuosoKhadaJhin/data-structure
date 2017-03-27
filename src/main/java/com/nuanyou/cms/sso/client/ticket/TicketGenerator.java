package com.nuanyou.cms.sso.client.ticket;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Felix on 2017/3/22.
 */
public class TicketGenerator {

//    public String getNewTicketId(final String prefix) {
//        final String number = this.getNextNumberAsString();
//        final StringBuilder buffer = new StringBuilder(prefix.length() + 2
//                + (this.suffix != null ? this.suffix.length() : 0) + this.randomStringGenerator.getMaxLength()
//                + number.length());
//
//        buffer.append(prefix);
//        buffer.append("-");
//        buffer.append(number);
//        buffer.append("-");
//        buffer.append(this.randomStringGenerator.getNewString());
//
//        if (this.suffix != null) {
//            buffer.append(this.suffix);
//        }
//
//        return buffer.toString();
//    }
//
//    public String getNextNumberAsString() {
//        return Long.toString(this.getNextValue());
//    }

   private final AtomicLong count= new AtomicLong(1000);

    public static void main1(String[] args) {
         SecureRandom randomizer = new SecureRandom();
        final byte[] random = new byte[50];

        randomizer.nextBytes(random);
    }


    protected String getNextValue() {
        if (this.count.compareAndSet(Long.MAX_VALUE, 0)) {
            return Long.toString(Long.MAX_VALUE);
        }
        return  Long.toString(this.count.getAndIncrement());
    }


}
