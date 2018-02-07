FROM tomcat:8.5.12-jre8-alpine
MAINTAINER Piotr Nowak <piotr_nowak@outlook.com>

# Copy built ninjabooks war
COPY target/nb-backend-*.war /usr/local/tomcat/webapps/

# Clean-up and rename app
RUN cd /usr/local/tomcat/webapps/ && \
	rm -rf ROOT && \
	mv nb-backend-*.war ROOT.war && \
	mkdir -p /scripts/

CMD ["catalina.sh", "run"]
