[![Actions Status](https://github.com/Hakky54/senzu/workflows/Build/badge.svg)](https://github.com/Hakky54/senzu/actions)
[![Apache2 license](https://img.shields.io/badge/license-Aache2.0-blue.svg)](https://github.com/Hakky54/sslcontext-kickstart/blob/master/LICENSE)

# Senzu 🫘
A CLI tool to get the battery percentage

## Demo
![alt text](https://github.com/Hakky54/senzu/blob/master/images/demo.gif?raw=true)

## Installing

TODO

## Build locally
<details>
  <summary>Build native executable</summary>

  **Minimum requirements:**
  1. GraalVM 17 with Native Image
  2. Maven
  3. Terminal
  
  **Additional OS specific requirements**
  - Linux: `sudo apt-get update && sudo apt-get install build-essential libz-dev zlib1g-dev -y`
  - Mac: `xcode-select --install`
  - Windows: Visual Studio app
  
  ```text
  mvn clean install -Pnative-image \
   && ./target/senzu
  ```
  
  The os native executable binary will be available under the target directory having the file name `senzu`

</details>

<details>
  <summary>Build java fat jar</summary>

  **Minimum requirements:**
  1. Java 8
  2. Maven
  3. Terminal

  ```text
  mvn clean install \
   && java -jar target/senzu.jar
  ```

  The fat jar will be available under the target directory having the file name `senzu.jar`

</details>

## Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Make a donation through [GitHub](https://github.com/sponsors/Hakky54) or [open collective](https://opencollective.com/hakky54)
* Submit a PR
