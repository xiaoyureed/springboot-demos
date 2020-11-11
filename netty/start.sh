# heartbeat
mvn clean package
java -jar ./target/*.jar --spring.profiles.active=server
java -jar ./target/*.jar --spring.profiles.active=client0
java -jar ./target/*.jar --spring.profiles.active=client1