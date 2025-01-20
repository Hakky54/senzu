# Battery 🔋
A CLI tool to get the battery percentage

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
   && ./target/battery
  ```
  
  The os native executable binary will be available under the target directory having the file name `battery`

</details>

<details>
  <summary>Build java fat jar</summary>

  **Minimum requirements:**
  1. Java 8
  2. Maven
  3. Terminal

  ```text
  mvn clean install \
   && java -jar target/battery.jar
  ```

  The fat jar will be available under the target directory having the file name `battery.jar`

</details>

## Contributing

There are plenty of ways to contribute to this project:

* Give it a star
* Make a donation through [GitHub](https://github.com/sponsors/Hakky54) or [open collective](https://opencollective.com/hakky54)
* Submit a PR
