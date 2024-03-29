package com.asdflj.oclanl.asm;

import java.net.InetAddress;

import li.cil.oc.Settings;
import li.cil.oc.server.component.InternetCard$;

public class Hooker {

    public static boolean isRequestAllowed(Settings settings, InetAddress inetAddress, String host) {
        return inetAddress.isAnyLocalAddress() || inetAddress.isLoopbackAddress()
            || inetAddress.isLinkLocalAddress()
            || inetAddress.isSiteLocalAddress()
            || InternetCard$.MODULE$.isRequestAllowed(settings, inetAddress, host);
    }
}
