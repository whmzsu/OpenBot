package org.openbot;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdManager.DiscoveryListener;
import android.net.nsd.NsdManager.ResolveListener;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

class NsdService {

  private final String SERVICE_TYPE = "_http._tcp.";

  private final DiscoveryListener mDiscoveryListener =
      new DiscoveryListener() {

        @Override
        public void onDiscoveryStarted(String serviceType) {}

        @Override
        public void onServiceFound(NsdServiceInfo service) {
          // A service was found!  Do something with it.
          String name = service.getServiceName();
          String type = service.getServiceType();
          Log.d("NSD", "Service Name=" + name);
          Log.d("NSD", "Service Type=" + type);
          if (type.equals(SERVICE_TYPE) && name.contains("Openbot")) {
            mNsdManager.resolveService(service, mResolveListener);
          }
        }

        @Override
        public void onServiceLost(NsdServiceInfo serviceInfo) {
          // When the network service is no longer available.
          // Internal bookkeeping code goes here.
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {}

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
          mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
          mNsdManager.stopServiceDiscovery(this);
        }
      };

  private ResolveListener mResolveListener;
  private NsdManager mNsdManager;

  public void start(Context context, ResolveListener resolveListener) {
    this.mResolveListener = resolveListener;
    Log.d("NSD", "Start discovery");
    mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);
    mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
  }

  public void stop() {
    Log.d("NSD", "Stop discovery");
    mNsdManager.stopServiceDiscovery(mDiscoveryListener);
  }
}
