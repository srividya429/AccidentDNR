package com.cyient.iot.ambulance;

import java.security.AccessController;
import java.security.Provider;

/**
 * Created by admin on 26-10-2017.
 */

public class JSSEProvider extends Provider {
    /**
     * Constructs a provider with the specified name, version number,
     * and information.
     *
     * @param name    the provider name.
     * @param version the provider version number.
     * @param info    a description of the provider and its services.
     */
    protected JSSEProvider() {
        super("HarmonyJSSE", 1.0, "Harmony JSSE Provider");
        AccessController
                .doPrivileged(new java.security.PrivilegedAction<Void>() {
                    public Void run() {
                        put("SSLContext.TLS",
                                "org.apache.harmony.xnet.provider.jsse.SSLContextImpl");
                        put("Alg.Alias.SSLContext.TLSv1", "TLS");
                        put("KeyManagerFactory.X509",
                                "org.apache.harmony.xnet.provider.jsse.KeyManagerFactoryImpl");
                        put("TrustManagerFactory.X509",
                                "org.apache.harmony.xnet.provider.jsse.TrustManagerFactoryImpl");
                        return null;
                    }
                });
    }
}
