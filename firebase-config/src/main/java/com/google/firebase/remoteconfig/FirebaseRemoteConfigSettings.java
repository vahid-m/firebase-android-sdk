// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
//
// You may obtain a copy of the License at
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.remoteconfig;

import static com.google.firebase.remoteconfig.RemoteConfigComponent.NETWORK_CONNECTION_TIMEOUT_IN_SECONDS;
import static com.google.firebase.remoteconfig.internal.ConfigFetchHandler.DEFAULT_MINIMUM_FETCH_INTERVAL_IN_SECONDS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.Proxy;

/**
 * Wraps the settings for {@link FirebaseRemoteConfig} operations.
 *
 * @author Lucas Png
 */
public class FirebaseRemoteConfigSettings {
  private final boolean enableDeveloperMode;
  private final long fetchTimeoutInSeconds;
  private final long minimumFetchInterval;
  private final Proxy.Type proxyType;
  private final String proxyHost;
  private final int proxyPort;

  private FirebaseRemoteConfigSettings(Builder builder) {
    enableDeveloperMode = builder.enableDeveloperMode;
    fetchTimeoutInSeconds = builder.fetchTimeoutInSeconds;
    minimumFetchInterval = builder.minimumFetchInterval;
    proxyType = builder.proxyType;
    proxyHost = builder.proxyHost;
    proxyPort = builder.proxyPort;
  }

  /**
   * Indicates the status of the developer mode setting.
   *
   * @return <code>true</code> if the developer mode is enabled, <code>false</code> otherwise.
   * @deprecated Use {@link #getMinimumFetchIntervalInSeconds()} instead.
   */
  @Deprecated
  public boolean isDeveloperModeEnabled() {
    return enableDeveloperMode;
  }

  /**
   * Returns the fetch timeout in seconds.
   *
   * <p>The timeout specifies how long the client should wait for a connection to the Firebase
   * Remote Config servers.
   */
  public long getFetchTimeoutInSeconds() {
    return fetchTimeoutInSeconds;
  }

  /** Returns the minimum interval between successive fetches calls in seconds. */
  public long getMinimumFetchIntervalInSeconds() {
    return minimumFetchInterval;
  }

  @NonNull
  public Proxy.Type getProxyType() {
    return proxyType;
  }

  @Nullable
  public String getProxyHost() {
    return proxyHost;
  }

  public int getProxyPort() {
    return proxyPort;
  }

  /** Constructs a builder initialized with the current FirebaseRemoteConfigSettings. */
  @NonNull
  public FirebaseRemoteConfigSettings.Builder toBuilder() {
    FirebaseRemoteConfigSettings.Builder frcBuilder = new FirebaseRemoteConfigSettings.Builder();
    frcBuilder.setDeveloperModeEnabled(this.isDeveloperModeEnabled());
    frcBuilder.setFetchTimeoutInSeconds(this.getFetchTimeoutInSeconds());
    frcBuilder.setMinimumFetchIntervalInSeconds(this.getMinimumFetchIntervalInSeconds());
    return frcBuilder;
  }

  /** Builder for a {@link FirebaseRemoteConfigSettings}. */
  public static class Builder {
    private boolean enableDeveloperMode = false;
    // TODO(issues/257): Move constants to Constants file.
    private long fetchTimeoutInSeconds = NETWORK_CONNECTION_TIMEOUT_IN_SECONDS;
    private long minimumFetchInterval = DEFAULT_MINIMUM_FETCH_INTERVAL_IN_SECONDS;
    @NonNull public Proxy.Type proxyType = Proxy.Type.DIRECT;
    @Nullable public String proxyHost;
    public int proxyPort;

    /**
     * Turns the developer mode setting on or off.
     *
     * @param enabled Should be <code>true</code> to enable, or <code>false</code> to disable this
     *     setting.
     * @deprecated Use {@link #setMinimumFetchIntervalInSeconds(long)} instead.
     */
    @NonNull
    @Deprecated
    public Builder setDeveloperModeEnabled(boolean enabled) {
      enableDeveloperMode = enabled;
      return this;
    }

    /**
     * Sets the connection timeout for fetch requests to the Firebase Remote Config servers in
     * seconds.
     *
     * <p>A fetch call will fail if it takes longer than the specified timeout to connect to the
     * Remote Config servers.
     *
     * @param duration Timeout duration in seconds. Should be a non-negative number.
     */
    @NonNull
    public Builder setFetchTimeoutInSeconds(long duration) throws IllegalArgumentException {
      if (duration < 0) {
        throw new IllegalArgumentException(
            String.format(
                "Fetch connection timeout has to be a non-negative number. "
                    + "%d is an invalid argument",
                duration));
      }
      fetchTimeoutInSeconds = duration;
      return this;
    }

    /**
     * Sets the minimum interval between successive fetch calls.
     *
     * <p>Fetches less than {@code duration} seconds after the last fetch from the Firebase Remote
     * Config server would use values returned during the last fetch.
     *
     * @param duration Interval duration in seconds. Should be a non-negative number.
     */
    @NonNull
    public Builder setMinimumFetchIntervalInSeconds(long duration) {
      if (duration < 0) {
        throw new IllegalArgumentException(
            "Minimum interval between fetches has to be a non-negative number. "
                + duration
                + " is an invalid argument");
      }
      minimumFetchInterval = duration;
      return this;
    }

    /**
     * Sets the proxy server for client calls.
     *
     */
    @NonNull
    public Builder setProxyServer(@NonNull Proxy.Type type, @NonNull String host, int port) {
      this.proxyType = type;
      this.proxyHost = host;
      this.proxyPort = port;
      return this;
    }

    /**
     * Returns a {@link FirebaseRemoteConfigSettings} with the settings provided to this builder.
     */
    @NonNull
    public FirebaseRemoteConfigSettings build() {
      return new FirebaseRemoteConfigSettings(this);
    }
  }
}
