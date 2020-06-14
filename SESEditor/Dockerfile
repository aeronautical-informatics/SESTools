FROM jamesdbloom/docker-java8-maven

RUN mkdir ~/.vnc
#RUN Xvfb 35.246.243.229:5901 &
RUN export DISPLAY=35.246.243.229:5901
ARG DISPLAY=35.246.243.229:5901

COPY . /app
WORKDIR /app

RUN ping 35.246.243.229
#RUN mvn $MAVEN_CLI_OPTS compile test
#RUN set
