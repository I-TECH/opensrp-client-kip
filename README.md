[![Build Status](https://travis-ci.org/OpenSRP/opensrp-client-path.svg?branch=master)](https://travis-ci.org/OpenSRP/opensrp-client-path) [![Coverage Status](https://coveralls.io/repos/github/OpenSRP/opensrp-client-path/badge.svg?branch=master)](https://coveralls.io/github/OpenSRP/opensrp-client-path?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4b06c9464e474ae0b2c369fa328c5c91)](https://www.codacy.com/app/OpenSRP/opensrp-client-path?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=OpenSRP/opensrp-client-path&amp;utm_campaign=Badge_Grade)

[![Dristhi](https://raw.githubusercontent.com/OpenSRP/opensrp-client/master/opensrp-app/res/drawable-mdpi/login_logo.png)](https://smartregister.atlassian.net/wiki/dashboard.action)

To run this project, you need to do this:
=========================================

1. Set the `ANDROID_HOME` environment variable to point to the location of your installed Android SDK 4.1.2 API level 16. For more information, look at [the documentation of maven-android-plugin](http://code.google.com/p/maven-android-plugin/wiki/GettingStarted).

2. Start an Android Virtual Device. Normally, this means you need to run `android avd` and then start one of the devices there.

Then, you can run `mvn clean install` in the main directory.

Tips and tricks (to be completed):
=================================

* How to setup your Android SDK so that Maven finds it: http://pivotal.github.com/robolectric/maven-quick-start.html

* Adding an external Android library (apklib) as a submodule, and making it work with both Maven and IntelliJ. TODO: Write about `mvn clean` trick.

Login (for demo server):
=================================
```
login-username - demotest
login-password - Demot123
```

Check `app.properties` file in `drishti-app/asset/` folder to change the demo server url to your own instance of opensrp server.
