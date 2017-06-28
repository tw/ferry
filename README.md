# Ferry
Ferry is an example money transfer API for Revolut.

### Assumptions
Some assumptions have been made when building this API.
* Firstly, the base currency is assumed to be GBP, and all transactions are assumed to be in GBP.
* It will be internal/and/or authentication will be handled seperately.
* The accounts are assumed to have no overdraft facility.

### Requirements
* Docker
* Java 8
* Maven

### Building
* `mvn package`

### Running normally (without Docker)
* `java -jar target/ferry-standalone.jar server conf/default.yml`
* `API will be available on http://localhost:3000`

### Running with Docker
* `docker build -t ferry .`
* `docker run -P3000:3000 ferry`
* `API will be available on http://localhost:3000`