FROM openjdk:11-jre
COPY target/universal/playskool-1.0.zip /tmp
RUN /usr/bin/unzip /tmp/playskool-1.0.zip
RUN mv /playskool-1.0 /usr/share/playskool
CMD ["/usr/share/playskool/bin/playskool"]
