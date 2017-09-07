FROM mykro/java8-jre:latest

COPY start.sh /usr/bin/

ADD ${project.artifactId}-${project.version}.jar /tmp/${project.artifactId}.jar

RUN chmod a+x /usr/bin/start.sh

CMD ["/usr/bin/start.sh"]