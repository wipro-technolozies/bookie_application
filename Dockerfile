FROM tomcat:8
USER root
copy context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
copy tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml
copy Locanda_target/*.war /usr/local/tomcat/webapps
CMD ["catalina.sh", "run"]
