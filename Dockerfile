FROM java:8

MAINTAINER galapagos-dev@bmc.com

COPY . workdir/

WORKDIR workdir

RUN GRADLE_USER_HOME=cache ./gradlew buildDeb -x test

RUN dpkg -i ./orca-web/build/distributions/*.deb


# change WORKDIR to workdir/bmc. It is relative to the current WORKDIR
WORKDIR bmc

RUN GRADLE_USER_HOME=cache ./gradlew buildDeb -x test

RUN dpkg -i ./orca-web/build/distributions/*.deb


CMD ["/opt/orca/bin/orca"]
