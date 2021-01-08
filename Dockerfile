FROM openjdk:12-alpine
ARG DEPENDENCY=target/dependency
ADD ${DEPENDENCY}/BOOT-INF/lib /app/lib
ADD ${DEPENDENCY}/META-INF /app/META-INF
ADD ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8081
ENV spring.profiles.active=qa
ENTRYPOINT ["java","-cp","app:app/lib/*","com.ecommerce.ECommerceApplication"]