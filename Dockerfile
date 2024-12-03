# 1. Java base image'ini kullan
FROM openjdk:21-jdk-slim

## Chrome ve ChromeDriver'ı yükle
## Install dependencies
#RUN apt-get update && apt-get install -y \
#  wget \
#  unzip \
#  curl \
#  libx11-dev \
#  libxcomposite-dev \
#  libxdamage-dev \
#  libxi6 \
#  libgdk-pixbuf2.0-0 \
#  libnspr4 \
#  libnss3 \
#  libatk1.0-0 \
#  libatk-bridge2.0-0 \
#  libgtk-3-0 \
#  libdbus-1-3 \
#  xdg-utils
#
## Install Chrome
#RUN wget https://storage.googleapis.com/chrome-for-testing-public/131.0.6778.85/linux64/chrome-linux64.zip -O /chrome.zip && \
#    unzip /chrome.zip -d /opt && \
#    rm /chrome.zip
#
## Install ChromeDriver
#RUN wget https://storage.googleapis.com/chrome-for-testing-public/131.0.6778.85/linux64/chromedriver-linux64.zip -O /chromedriver.zip && \
#    unzip /chromedriver.zip -d /usr/local/bin && \
#    rm /chromedriver.zip
#
## Set Chrome path
#ENV CHROME_BIN=/opt/google/chrome/chrome

# 2. Maven'ı yükle
RUN apt-get update && apt-get install -y maven

# 3. Çalışma dizinini belirle
WORKDIR /app

# 4. GitHub repo'dan tüm dosyaları kopyala
COPY . /app

# 5. Maven ile projeyi derle (jar dosyasını oluştur)
RUN mvn clean package -DskipTests

# 6. Jar dosyasını container'a kopyala
# COPY /app/target/fenerbahce-notifier-0.0.1-SNAPSHOT.jar fenerbahce-notifier.jar

# 7. Uygulamayı çalıştır
CMD ["java", "-jar", "/app/target/pos-api-1.0.0.jar"]

# Spring Boot varsayılan portunu aç
EXPOSE 8080