FROM java:7

EXPOSE 9000

# Based on https://hub.docker.com/r/ifinavet/playframework/

# Install Play version
ENV PLAY_VERSION 2.2.1

RUN wget -q https://downloads.typesafe.com/play/${PLAY_VERSION}/play-${PLAY_VERSION}.zip \
    && unzip -q play-${PLAY_VERSION}.zip \
    && rm play-${PLAY_VERSION}.zip \
    && ln -s /play-${PLAY_VERSION}/play /usr/local/bin/


# Choose a work directory
WORKDIR /app

# Copy project/ which holds your project definitions
COPY project project

# Trigger dependency download
RUN play help

# Copy your entire project
COPY ./ ./

RUN play -Dsbt.log.noformat=true clean compile test stage

COPY docker/application.conf ./conf/application.conf

ENV JAVA_OPTS -Xms250m -Xmx3000m -XX:MaxPermSize=750m -XX:ReservedCodeCacheSize=375m -XX:+CMSClassUnloadingEnabled -Dfile.encoding=UTF-8 -Dpidfile.path=/dev/null

# Your start command
CMD play start
