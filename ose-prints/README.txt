keytool -genkey -alias tomcat -dname "CN=OSE,OU=OSE,O=OSE,L=Shanghai,ST=Shanghai,C=CN" -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore ./keystore.p12 -validity 3650
